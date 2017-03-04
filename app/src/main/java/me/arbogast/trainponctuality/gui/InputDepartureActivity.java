package me.arbogast.trainponctuality.gui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.dbaccess.RoutesDAO;
import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.dbaccess.TripsDAO;
import me.arbogast.trainponctuality.model.Line;
import me.arbogast.trainponctuality.model.LineAdapter;
import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.services.LocationProxy;

public class InputDepartureActivity extends AppCompatActivity {
    private static final int RESULT_GET_DEPARTURE_STATION = 0;
    private static final String TAG = "InputDepartureActivity";

    private EditText txtDepartureDate;
    private EditText txtDepartureTime;
    private TextView txtDepartureStation;
    private Spinner spnLine;
    private AutoCompleteTextView spnMission;

    private Stops departureStation;
    private Line selectedLine;

    private Observer locationObserver;
    private Location foundLocation;
    private ArrayList<Stops> stationList;
    private boolean manualStationSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_departure);
        setTitle(R.string.titleDeparture);

        txtDepartureDate = (EditText) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (EditText) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (TextView) findViewById(R.id.txtLocation);
        spnLine = (Spinner) findViewById(R.id.spnLine);
        spnMission = (AutoCompleteTextView) findViewById(R.id.spnMission);

        txtDepartureDate.setText(Utils.getDate());
        txtDepartureTime.setText(Utils.getTime());

        spnLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Line newLine = (Line) spnLine.getSelectedItem();
                if (selectedLine == null || !newLine.getCode().equals(selectedLine.getCode())) {
                    setSelectedLine(newLine);
                    setDepartureStation(null);
                    populateMissions();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelectedLine(null);
                setDepartureStation(null);
                populateMissions();
            }
        });

        try (RoutesDAO dbRoutes = new RoutesDAO(this)) {
            LineAdapter adapter = new LineAdapter(this, R.layout.spinner_line_layout, dbRoutes.getDistinctLines());
            spnLine.setAdapter(adapter);
        }

        populateMissions(((Line) spnLine.getSelectedItem()).getCode());

        locationObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i(TAG, "update: Stopping Location");
                LocationProxy.getInstance().stopRequest(getApplicationContext());
                foundLocation = LocationProxy.getInstance().getLastBest();
                autoSelectStation();
            }
        };
        Log.i(TAG, "onCreate: AddObserver");
        LocationProxy.getInstance().addObserver(locationObserver);
    }

    private void autoSelectStation() {
        if (manualStationSelected || selectedLine == null)
            return;

        GetStationForLineAsync findStationAsync = new GetStationForLineAsync() {
            @Override
            protected void onPostExecute(ArrayList<Stops> stops) {
                super.onPostExecute(stops);
                stationList = stops;
                setDepartureStation(stops.get(0));
            }
        };

        findStationAsync.execute(new GetStationForLineParams(this, selectedLine.getCode(), foundLocation));
    }

    private void setSelectedLine(Line value) {
        stationList = null;
        selectedLine = value;
        autoSelectStation();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: RemoveObserver");
        LocationProxy.getInstance().deleteObserver(locationObserver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        LocationProxy.getInstance().startRequest(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        LocationProxy.getInstance().stopRequest(this);

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("departureStation", departureStation);
        outState.putString("selectedLine", selectedLine.getCode());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String lineCode = savedInstanceState.getString("selectedLine");
        if (lineCode != null && !lineCode.isEmpty())
            setSelectedLine(new Line(lineCode));
        setDepartureStation((Stops) savedInstanceState.getParcelable("departureStation"));
    }

    private void populateMissions() {
        populateMissions(null);
    }

    private void populateMissions(String select) {
        if (selectedLine == null)
            spnMission.setAdapter(null);
        else {
            try (TripsDAO dbTrips = new TripsDAO(this)) {
                ArrayAdapter missionAdapter = new ArrayAdapter<>(this, R.layout.spinner_row_text, dbTrips.getTripsForLine(select != null ? select : selectedLine.getCode()));
                spnMission.setAdapter(missionAdapter);
            }
        }
    }

    public void ValidateDeparture(View view) {
        if (departureStation == null || selectedLine == null)
            return;

        Travel departure = new Travel(Utils.parseDate(Utils.getText(txtDepartureDate), Utils.getText(txtDepartureTime)),
                selectedLine.getCode(), spnMission.getText().toString(), departureStation.getId());

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.insert(departure);
        }
        finish();
    }

    public void CancelInputDeparture(View view) {
        finish();
    }

    public void showStationList(View view) {
        if(stationList == null)
            return;
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", R.string.txtLocationHint);
        showList.putExtra("color", R.color.stationSelection);
        showList.putParcelableArrayListExtra("stops", stationList);
        startActivityForResult(showList, RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_GET_DEPARTURE_STATION && resultCode == RESULT_OK && data != null) {
            Stops selected = data.getExtras().getParcelable("stop");
            if (selected == null)
                return;

            manualStationSelected = true;
            setDepartureStation(selected);
        }
    }

    public void setDepartureStation(Stops departureStation) {
        if (departureStation == null) {
            txtDepartureStation.setText(R.string.txtLocationHint);
            return;
        }

        this.departureStation = departureStation;
        txtDepartureStation.setText(departureStation.getName());
    }
}
