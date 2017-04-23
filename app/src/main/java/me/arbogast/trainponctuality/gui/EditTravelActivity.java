package me.arbogast.trainponctuality.gui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.dbaccess.RoutesDAO;
import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.dbaccess.TripsDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.Line;
import me.arbogast.trainponctuality.model.LineAdapter;
import me.arbogast.trainponctuality.model.Stops;

public class EditTravelActivity extends AppCompatActivity {
    private History currentTravel;
    private ArrayList<Stops> stationList;
    private List<String> missionsAvailable;

    private TextView txtDepartureDate;
    private TextView txtDepartureTime;
    private TextView txtDepartureStation;
    private Spinner spnLine;
    private AutoCompleteTextView actMission;
    private TextView txtArrivalDate;
    private TextView txtArrivalTime;
    private TextView txtArrivalStation;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_travel);
        setTitle(R.string.titleEditTravel);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.editionHeader)));

        txtDepartureDate = (TextView) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (TextView) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (TextView) findViewById(R.id.txtDepartureLocation);
        spnLine = (Spinner) findViewById(R.id.spnLine);
        actMission = (AutoCompleteTextView) findViewById(R.id.actMission);
        txtArrivalDate = (TextView) findViewById(R.id.txtArrivalDate);
        txtArrivalTime = (TextView) findViewById(R.id.txtArrivalTime);
        txtArrivalStation = (TextView) findViewById(R.id.txtArrivalLocation);

        Bundle data = getIntent().getExtras();
        currentTravel = data.getParcelable("history");

        try (RoutesDAO dbRoutes = new RoutesDAO(this)) {
            LineAdapter adapter = new LineAdapter(this, R.layout.spinner_line_layout, dbRoutes.getDistinctLines());
            spnLine.setAdapter(adapter);
        }

        setDepartureDateTexts();
        setArrivalDateTexts();
        for (int i = 0; i < spnLine.getCount(); i++) {
            if (((Line) spnLine.getItemAtPosition(i)).getCode().equals(currentTravel.getTravel().getLine())) {
                spnLine.setSelection(i);
                break;
            }
        }
        txtDepartureStation.setText(currentTravel.getDepartureStation());
        txtArrivalStation.setText(currentTravel.getArrivalStation());
        actMission.setText(currentTravel.getTravel().getMissionCode());

        spnLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Line newLine = (Line) spnLine.getSelectedItem();
                if (!newLine.getCode().equals(currentTravel.getTravel().getLine())) {
                    setSelectedLine(newLine);
                    setDepartureStation(null);
                    setArrivalStation(null);
                    populateMissions();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelectedLine(null);
                setDepartureStation(null);
                setArrivalStation(null);
                populateMissions();
            }
        });

        populateMissions();
        findStations();
    }

    private void populateMissions() {
        try (TripsDAO dbTrips = new TripsDAO(this)) {
            missionsAvailable = dbTrips.getTripsForLine(currentTravel.getTravel().getLine());
            ArrayAdapter missionAdapter = new ArrayAdapter<>(this, R.layout.spinner_row_text, missionsAvailable);
            actMission.setAdapter(missionAdapter);
        }
    }

    private void findStations() {
        GetStationForLineAsync findStationAsync = new GetStationForLineAsync() {
            @Override
            protected void onPostExecute(ArrayList<Stops> stops) {
                super.onPostExecute(stops);
                stationList = stops;
            }
        };

        String missionFilter = null;
        if (actMission.getText().length() == 4 && missionsAvailable.indexOf(actMission.getText().toString()) >= 0)
            missionFilter = actMission.getText().toString();

        findStationAsync.execute(new GetStationForLineParams(this, currentTravel.getTravel().getLine(), null, missionFilter));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentTravel", currentTravel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTravel = savedInstanceState.getParcelable("currentTravel");
        populateMissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_validate_cancel_edition, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void CancelEdition(MenuItem item) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void ValidateEdition(MenuItem item) {
        currentTravel.getTravel().setMissionCode(actMission.getText().toString());
        if (currentTravel.getTravel().isInValidTravel())
            return;

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.update(currentTravel.getTravel());
        }

        Intent ret = new Intent();
        ret.putExtra("history", currentTravel);
        setResult(RESULT_OK, ret);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Stops selected;

        if (resultCode != RESULT_OK || data == null)
            return;

        switch (requestCode) {
            case Utils.RESULT_GET_DEPARTURE_STATION:
                selected = data.getExtras().getParcelable("stop");
                if (selected == null)
                    return;
                setDepartureStation(selected);
                break;

            case Utils.RESULT_GET_DEPARTURE_DATE:
            case Utils.RESULT_GET_DEPARTURE_TIME:
                setDepartureDateTime(data.getExtras().getLong("date"));
                break;

            case Utils.RESULT_GET_ARRIVAL_STATION:
                selected = data.getExtras().getParcelable("stop");
                if (selected == null)
                    return;
                setArrivalStation(selected);
                break;

            case Utils.RESULT_GET_ARRIVAL_DATE:
            case Utils.RESULT_GET_ARRIVAL_TIME:
                setArrivalDateTime(data.getExtras().getLong("date"));
                break;
        }
    }

    private void setSelectedLine(Line value) {
        stationList = null;
        currentTravel.getTravel().setLine(value.getCode());
        findStations();
    }

    public void setDepartureStation(Stops departureStation) {
        if (departureStation == null) {
            txtDepartureStation.setText(R.string.txtLocationHint);
            return;
        }

        currentTravel.getTravel().setDepartureStation(departureStation.getId());
        currentTravel.setDepartureStation(departureStation.getName());
        txtDepartureStation.setText(departureStation.getName());
    }

    public void setArrivalStation(Stops arrivalStation) {
        if (arrivalStation == null) {
            txtArrivalStation.setText(R.string.txtLocationArrivalHint);
            return;
        }

        currentTravel.getTravel().setArrivalStation(arrivalStation.getId());
        currentTravel.setArrivalStation(arrivalStation.getName());
        txtArrivalStation.setText(arrivalStation.getName());
    }

    private void setDepartureDateTime(Long millis) {
        currentTravel.getTravel().setDepartureDate(Utils.millisToDate(millis));
        currentTravel.setDayTravel(Utils.dateToString(currentTravel.getTravel().getDepartureDate()));
        setDepartureDateTexts();
    }

    private void setDepartureDateTexts() {
        txtDepartureDate.setText(Utils.dateToString(currentTravel.getTravel().getDepartureDate()));
        txtDepartureTime.setText(Utils.timeToString(currentTravel.getTravel().getDepartureDate()));
    }

    private void setArrivalDateTime(Long millis) {
        currentTravel.getTravel().setArrivalDate(Utils.millisToDate(millis));
        setArrivalDateTexts();
    }

    private void setArrivalDateTexts() {
        txtArrivalDate.setText(Utils.dateToString(currentTravel.getTravel().getArrivalDate()));
        txtArrivalTime.setText(Utils.timeToString(currentTravel.getTravel().getArrivalDate()));
    }

    public void TextDepartureDateClicked(View view) {
        Intent intent = new Intent(this, DateSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureDateHint);
        intent.putExtra("date", currentTravel.getTravel().getDepartureDate().getTime());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_DEPARTURE_DATE);
    }

    public void TextDepartureTimeClicked(View view) {
        Intent intent = new Intent(this, TimeSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureTimeHint);
        intent.putExtra("date", currentTravel.getTravel().getDepartureDate().getTime());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_DEPARTURE_TIME);
    }

    public void TextArrivalDateClicked(View view) {
        Intent intent = new Intent(this, DateSelectionActivity.class);
        intent.putExtra("title", R.string.txtArrivalDateHint);
        intent.putExtra("date", currentTravel.getTravel().getArrivalDate().getTime());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_ARRIVAL_DATE);
    }

    public void TextArrivalTimeClicked(View view) {
        Intent intent = new Intent(this, TimeSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureTimeHint);
        intent.putExtra("date", currentTravel.getTravel().getArrivalDate().getTime());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, Utils.RESULT_GET_ARRIVAL_TIME);
    }

    public void showDepartureStationList(View view) {
        if (stationList == null)
            return;
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", R.string.txtLocationHint);
        showList.putExtra("color", R.color.stationSelection);
        showList.putParcelableArrayListExtra("stops", stationList);
        startActivityForResult(showList, Utils.RESULT_GET_DEPARTURE_STATION);
    }

    public void showArrivalStationList(View view) {
        if (stationList == null)
            return;
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", R.string.txtLocationArrivalHint);
        showList.putExtra("color", R.color.stationSelection);
        showList.putParcelableArrayListExtra("stops", stationList);
        startActivityForResult(showList, Utils.RESULT_GET_ARRIVAL_STATION);
    }
}
