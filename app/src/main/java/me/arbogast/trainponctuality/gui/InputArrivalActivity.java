package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.services.LocationProxy;

public class InputArrivalActivity extends Activity {
    private static final int RESULT_GET_DEPARTURE_STATION = 0;
    private static final String TAG = "InputArrivalActivity";

    private EditText txtArrivalDate;
    private EditText txtArrivalTime;
    private TextView txtArrivalStation;

    private Travel currentTravel;
    private Stops arrivalStation;

    private Observer locationObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_arrival);

        txtArrivalDate = (EditText) findViewById(R.id.txtArrivalDate);
        txtArrivalTime = (EditText) findViewById(R.id.txtArrivalTime);
        txtArrivalStation = (TextView) findViewById(R.id.txtLocation);

        txtArrivalDate.setText(Utils.getDate());
        txtArrivalTime.setText(Utils.getTime());

        Bundle data = getIntent().getExtras();
        currentTravel = data.getParcelable("travel");

        locationObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i(TAG, "update: Stopping Location");
                LocationProxy.getInstance().stopRequest(getApplicationContext());
            }
        };
        Log.i(TAG, "onCreate: AddObserver");
        LocationProxy.getInstance().addObserver(locationObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentTravel", currentTravel);
        outState.putParcelable("arrivalStation", arrivalStation);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTravel = savedInstanceState.getParcelable("currentTravel");
        arrivalStation = savedInstanceState.getParcelable("arrivalStation");
        setStationText();
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

    public void CancelInputArrival(View view) {
        finish();
    }

    public void ValidateArrival(View view) {
        currentTravel.setArrivalDate(Utils.parseDate(Utils.getText(txtArrivalDate), Utils.getText(txtArrivalTime)));
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.update(currentTravel);
        }
        finish();
    }

    public void showStationList(View view) {
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title", getString(R.string.txtLocationArrivalHint));
        showList.putExtra("color", R.color.stationSelection);
        showList.putExtra("line", currentTravel.getLine());
        startActivityForResult(showList, RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_GET_DEPARTURE_STATION && resultCode == RESULT_OK && data != null) {
            arrivalStation = data.getExtras().getParcelable("stop");
            if (arrivalStation == null)
                return;
            currentTravel.setArrivalStation(arrivalStation.getId());
            setStationText();
        }
    }

    private void setStationText() {
        if (arrivalStation != null)
            txtArrivalStation.setText(arrivalStation.getName());
        else
            txtArrivalStation.setText(R.string.txtLocationArrivalHint);
    }
}
