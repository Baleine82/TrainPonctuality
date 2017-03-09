package me.arbogast.trainponctuality.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.services.LocationProxy;

public class InputArrivalActivity extends AppCompatActivity {
    private static final int RESULT_GET_DEPARTURE_STATION = 0;
    private static final int RESULT_GET_ARRIVAL_DATE = 1;
    private static final int RESULT_GET_ARRIVAL_TIME = 2;
    private static final String TAG = "InputArrivalActivity";

    private TextView txtArrivalDate;
    private TextView txtArrivalTime;
    private TextView txtArrivalStation;

    private Travel currentTravel;
    private Stops arrivalStation;

    private Observer locationObserver;
    private boolean manualStationSelected = false;
    private ArrayList<Stops> stationList;
    private Calendar arrivalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_arrival);
        setTitle(R.string.titleArrival);

        txtArrivalDate = (TextView) findViewById(R.id.txtArrivalDate);
        txtArrivalTime = (TextView) findViewById(R.id.txtArrivalTime);
        txtArrivalStation = (TextView) findViewById(R.id.txtLocation);

        setArrivalDateTime(GregorianCalendar.getInstance());

        Bundle data = getIntent().getExtras();
        currentTravel = data.getParcelable("travel");

        locationObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i(TAG, "update: Stopping Location");
                LocationProxy.getInstance().stopRequest(getApplicationContext());
                autoSelectStation();
            }
        };
        Log.i(TAG, "onCreate: AddObserver");
        LocationProxy.getInstance().addObserver(locationObserver);
        autoSelectStation();
    }

    private void setArrivalDateTime(Calendar select) {
        arrivalDate = select;
        txtArrivalDate.setText(Utils.dateToString(arrivalDate.getTime()));
        txtArrivalTime.setText(Utils.timeToString(arrivalDate.getTime()));
    }
    private void setArrivalDateTime(Long millis) {
        arrivalDate.setTimeInMillis(millis);
        txtArrivalDate.setText(Utils.dateToString(arrivalDate.getTime()));
        txtArrivalTime.setText(Utils.timeToString(arrivalDate.getTime()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentTravel", currentTravel);
        outState.putParcelable("arrivalStation", arrivalStation);
        outState.putLong("arrivalDate", arrivalDate.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTravel = savedInstanceState.getParcelable("currentTravel");
        setArrivalStation((Stops) savedInstanceState.getParcelable("arrivalStation"));

        setArrivalDateTime(savedInstanceState.getLong("arrivalDate"));
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

    private void autoSelectStation() {
        if (manualStationSelected)
            return;

        GetStationForLineAsync findStationAsync = new GetStationForLineAsync() {
            @Override
            protected void onPostExecute(ArrayList<Stops> stops) {
                super.onPostExecute(stops);
                stationList = stops;
                setArrivalStation(stops.get(0));
            }
        };

        findStationAsync.execute(new GetStationForLineParams(this, currentTravel.getLine(), LocationProxy.getInstance().getLastBest()));
    }


    public void CancelInputArrival(View view) {
        finish();
    }

    public void ValidateArrival(View view) {
        currentTravel.setArrivalDate(arrivalDate.getTime());
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.update(currentTravel);
        }
        finish();
    }

    public void showStationList(View view) {
        if (stationList == null)
            return;
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", R.string.txtLocationArrivalHint);
        showList.putExtra("color", R.color.stationSelection);
        showList.putParcelableArrayListExtra("stops", stationList);
        startActivityForResult(showList, RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null)
            return;

        switch (requestCode) {
            case RESULT_GET_DEPARTURE_STATION:
                arrivalStation = data.getExtras().getParcelable("stop");
                if (arrivalStation == null)
                    return;

                manualStationSelected = true;
                setArrivalStation(arrivalStation);
                break;

            case RESULT_GET_ARRIVAL_DATE:
            case RESULT_GET_ARRIVAL_TIME:
                setArrivalDateTime(data.getExtras().getLong("date"));
                break;
        }
    }

    public void setArrivalStation(Stops arrivalStation) {
        this.arrivalStation = arrivalStation;

        if (arrivalStation == null) {
            txtArrivalStation.setText(R.string.txtLocationArrivalHint);
            return;
        }

        currentTravel.setArrivalStation(arrivalStation.getId());
        txtArrivalStation.setText(arrivalStation.getName());
    }

    public void TextArrivalDateClicked(View view) {
        Intent intent = new Intent(this, DateSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureDateHint);
        intent.putExtra("date", arrivalDate.getTimeInMillis());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, RESULT_GET_ARRIVAL_DATE);
    }

    public void TextArrivalTimeClicked(View view) {
        Intent intent = new Intent(this, TimeSelectionActivity.class);
        intent.putExtra("title", R.string.txtDepartureTimeHint);
        intent.putExtra("date", arrivalDate.getTimeInMillis());
        intent.putExtra("color", R.color.dateSelection);
        startActivityForResult(intent, RESULT_GET_ARRIVAL_TIME);
    }
}
