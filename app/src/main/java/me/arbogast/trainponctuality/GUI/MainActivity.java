package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.arbogast.trainponctuality.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void InputDepartureClick(View view) {
        Intent inputDeparture = new Intent(this, InputDepartureActivity.class);

        startActivity(inputDeparture);
    }

    public void InputArrivalClick(View view) {
    }

    public void btnHistoryClick(View view) {
    }
}
