package me.arbogast.trainponctuality.gui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.services.LocationProxy;
import me.arbogast.trainponctuality.sncfapi.InitializeSncfData;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_GPS = 1;
    private static final String TAG = "MainActivity";
    private Observer locationObserver;
    private Travel currentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i(TAG, "update: Stopping Location");
                LocationProxy.getInstance().stopRequest(getApplicationContext());
            }
        };
        Log.i(TAG, "onCreate: AddObserver");
        LocationProxy.getInstance().addObserver(locationObserver);

        //Initializing location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_GPS);
        else
            LocationProxy.getInstance().startRequest(this);
        new InitializeSncfData(getApplicationContext(), findViewById(R.id.txtStatus)).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            currentTravel = dbTravel.selectCurrentTravel();
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: RemoveObserver");
        LocationProxy.getInstance().deleteObserver(locationObserver);
        LocationProxy.getInstance().stopRequest(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        if (currentTravel == null)
            menu.findItem(R.id.mnu_arrival).setVisible(false);
        else
            menu.findItem(R.id.mnu_departure).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationProxy.getInstance().startRequest(this);
                }
            }
        }
    }

    public void InputDepartureClick(View view) {
        ShowDepartureActivity();
    }

    public void MenuDepartureClick(MenuItem item) {
        ShowDepartureActivity();
    }

    private void ShowDepartureActivity() {
        if (currentTravel == null) {
            Intent inputDeparture = new Intent(this, InputDepartureActivity.class);
            startActivity(inputDeparture);
        }
    }

    public void InputArrivalClick(View view) {
        ShowArrivalActivity();
    }

    public void MenuArrivalClick(MenuItem item) {
        ShowArrivalActivity();
    }

    private void ShowArrivalActivity() {
        if (currentTravel != null) {
            Intent inputArrival = new Intent(this, InputArrivalActivity.class);
            inputArrival.putExtra("travel", currentTravel);
            startActivity(inputArrival);
        }
    }

    public void InputHistoryClick(View view) {
        ShowHistoryActivity();
    }

    public void MenuHistoryClick(MenuItem item) {
        ShowHistoryActivity();
    }

    private void ShowHistoryActivity() {
        Intent showHistory = new Intent(this, ShowHistoryActivity.class);
        showHistory.putExtra("title", getString(R.string.historyHeader));
        showHistory.putExtra("color", R.color.historyHeader);
        startActivity(showHistory);
    }
}
