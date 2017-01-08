package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void InputDepartureClick(View view) {
        if (new TravelDAO(this).selectCurrentTravel() == null) {
            Intent inputDeparture = new Intent(this, InputDepartureActivity.class);

            startActivity(inputDeparture);
        }
    }

    public void InputArrivalClick(View view) {
        Travel currentTravel = new TravelDAO(this).selectCurrentTravel();

        if (currentTravel != null) {
            Intent inputArrival = new Intent(this, InputArrivalActivity.class);
            inputArrival.putExtra("travel", currentTravel);
            startActivity(inputArrival);
        }
    }

    public void btnHistoryClick(View view) {
        Intent showHistory = new Intent(this, ShowHistoryActivity.class);
        startActivity(showHistory);
    }
}
