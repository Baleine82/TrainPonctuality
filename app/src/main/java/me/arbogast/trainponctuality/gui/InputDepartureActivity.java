package me.arbogast.trainponctuality.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
    private static final String TAG = "InputDepartureActivity";

    private TextView txtDepartureDate;
    private TextView txtDepartureTime;
    private TextView txtDepartureStation;
    private Spinner spnLine;
    private AutoCompleteTextView actMission;

    private Stops departureStation;
    private Line selectedLine;
    private List<String> missionsAvailable;

    private Observer locationObserver;
    private ArrayList<Stops> stationList;
    private boolean manualStationSelected = false;
    private Calendar departureDate;
    private String tripId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_departure);
        setTitle(R.string.titleDeparture);

        txtDepartureDate = (TextView) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (TextView) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (TextView) findViewById(R.id.txtLocation);
        spnLine = (Spinner) findViewById(R.id.spnLine);
        actMission = (AutoCompleteTextView) findViewById(R.id.actMission);

        // This is not working, why?
        actMission.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromInputMethod(view.getApplicationWindowToken(), 0);
            }
        });

        actMission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                manualStationSelected = false;
                autoSelectStation();
            }
        });

        setDepartureDateTime(GregorianCalendar.getInstance());

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

        //populateMissions(((Line) spnLine.getSelectedItem()).getCode());

        locationObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i(TAG, "update: Stopping Location");
                LocationProxy.getInstance().stopRequest(getApplicationContext());
                //autoSelectStation();
            }
        };
        Log.i(TAG, "onCreate: AddObserver");
        LocationProxy.getInstance().addObserver(locationObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_validate_cancel_edition, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void CancelEdition(MenuItem item) {
        finish();
    }

    public void ValidateEdition(MenuItem item) {
        addTravel();
    }

    private void setDepartureDateTime(Calendar date) {
        departureDate = date;
        txtDepartureDate.setText(Utils.dateToString(departureDate.getTime()));
        txtDepartureTime.setText(Utils.timeToString(departureDate.getTime()));
    }

    private void setDepartureDateTime(Long millis) {
        departureDate.setTimeInMillis(millis);
        txtDepartureDate.setText(Utils.dateToString(departureDate.getTime()));
        txtDepartureTime.setText(Utils.timeToString(departureDate.getTime()));
    }

    private void autoSelectStation() {
        if (manualStationSelected || selectedLine == null)
            return;

        GetStationForLineAsync findStationAsync = new GetStationForLineAsync() {
            @Override
            protected void onPostExecute(ArrayList<Stops> stops) {
                super.onPostExecute(stops);
                stationList = stops;
                if (stops != null && stops.size() > 0)
                    setDepartureStation(stops.get(0));
            }
        };

        String missionFilter = null;

        if (actMission.getText().length() == 4 && missionsAvailable != null && missionsAvailable.indexOf(actMission.getText().toString()) >= 0)
            missionFilter = actMission.getText().toString();

        findStationAsync.execute(new GetStationForLineParams(this, selectedLine.getCode(), LocationProxy.getInstance().getLastBest(), missionFilter));
    }

    private void setSelectedLine(Line value) {
        manualStationSelected = false;
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
        outState.putLong("departureDate", departureDate.getTimeInMillis());
        outState.putString("trip", tripId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String lineCode = savedInstanceState.getString("selectedLine");
        if (lineCode != null && !lineCode.isEmpty())
            setSelectedLine(new Line(lineCode));
        setDepartureStation((Stops) savedInstanceState.getParcelable("departureStation"));

        setDepartureDateTime(savedInstanceState.getLong("departureDate"));
        String mission = actMission.getText().toString();
        populateMissions();
        actMission.setText(mission);

        tripId = savedInstanceState.getString("trip");
    }

    private void populateMissions() {
        actMission.setText(null);
        if (selectedLine == null)
            actMission.setAdapter(null);
        else {
            try (TripsDAO dbTrips = new TripsDAO(this)) {
                missionsAvailable = dbTrips.getTripsForLine(selectedLine.getCode());
                ArrayAdapter missionAdapter = new ArrayAdapter<>(this, R.layout.spinner_row_text, missionsAvailable);
                actMission.setAdapter(missionAdapter);
            }
        }
    }

    private void addTravel() {
        if (departureInputInvalid())
            return;

        Travel departure = new Travel(departureDate.getTime(), selectedLine.getCode(), actMission.getText().toString(), departureStation.getId(), tripId);

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.insert(departure);
        }
        finish();
    }

    private boolean departureInputInvalid() {
        return departureStation == null || selectedLine == null || actMission.getText().toString().equals("");
    }

    public void ValidateDeparture(View view) {
        addTravel();
    }

    public void CancelInputDeparture(View view) {
        finish();
    }

    public void showStationList(View view) {
        if (stationList == null)
            return;
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", R.string.txtLocationHint);
        showList.putExtra("color", R.color.stationSelection);
        showList.putParcelableArrayListExtra("stops", stationList);
        startActivityForResult(showList, Utils.RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null)
            return;

        switch (requestCode) {
            case Utils.RESULT_GET_DEPARTURE_STATION:
                Stops selected = data.getExtras().getParcelable("stop");
                if (selected == null)
                    return;

                manualStationSelected = true;
                setDepartureStation(selected);
                break;

            case Utils.RESULT_GET_DEPARTURE_DATE:
            case Utils.RESULT_GET_DEPARTURE_TIME:
                setDepartureDateTime(data.getExtras().getLong("date"));
                break;

            case Utils.RESULT_FIND_THEORIC_TRAVEL:
                tripId = data.getExtras().getString("trip");
                break;
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

    public void TextDepartureDateClicked(View view) {
        Intent intent = new Intent(this, DateSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureDateHint);
        intent.putExtra("date", departureDate.getTimeInMillis());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_DEPARTURE_DATE);
    }

    public void TextDepartureTimeClicked(View view) {
        Intent intent = new Intent(this, TimeSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureTimeHint);
        intent.putExtra("date", departureDate.getTimeInMillis());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_DEPARTURE_TIME);
    }

    public void findTheoricTravel(View view) {
        if (departureInputInvalid())
            return;

        Intent intent = new Intent(this, FindTheoricTravelActivity.class);
        intent.putExtra("line", selectedLine.getCode());
        intent.putExtra("mission", actMission.getText().toString());
        intent.putExtra("departureStation", departureStation);
        intent.putExtra("departureDate", departureDate.getTimeInMillis());
        startActivityForResult(intent, Utils.RESULT_FIND_THEORIC_TRAVEL);
    }
}
