package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.Travel;
import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.sncfapi.InitializeSncfData;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InitializeSncfData(getApplicationContext(), findViewById(R.id.txtStatus)).execute();
    }

    public void InputDepartureClick(View view) {
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            if (dbTravel.selectCurrentTravel() == null) {
                Intent inputDeparture = new Intent(this, InputDepartureActivity.class);

                startActivity(inputDeparture);
            }
        }
    }

    public void InputArrivalClick(View view) {
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            Travel currentTravel = dbTravel.selectCurrentTravel();

            if (currentTravel != null) {
                Intent inputArrival = new Intent(this, InputArrivalActivity.class);
                inputArrival.putExtra("travel", currentTravel);
                startActivity(inputArrival);
            }
        }
    }

    public void btnHistoryClick(View view) {
        Intent showHistory = new Intent(this, ShowHistoryActivity.class);
        showHistory.putExtra("title", getString(R.string.historyHeader));
        showHistory.putExtra("color", R.color.historyHeader);
        startActivity(showHistory);
    }
}
