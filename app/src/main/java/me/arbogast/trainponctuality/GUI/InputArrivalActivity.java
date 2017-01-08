package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class InputArrivalActivity extends Activity {
    private EditText txtArrivalDate;
    private EditText txtArrivalTime;
    private EditText txtArrivalStation;

    private Travel currentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_arrival);

        txtArrivalDate = (EditText) findViewById(R.id.txtArrivalDate);
        txtArrivalTime = (EditText) findViewById(R.id.txtArrivalTime);
        txtArrivalStation = (EditText) findViewById(R.id.txtLocation);

        txtArrivalDate.setText(Utils.getDate());
        txtArrivalTime.setText(Utils.getTime());

        Bundle data = getIntent().getExtras();
        currentTravel = data.getParcelable("travel");
    }

    public void CancelInputArrival(View view)  {
        finish();
    }

    public void ValidateArrival(View view) {
        currentTravel.setArrivalDate(Utils.parseDate(Utils.getText(txtArrivalDate), Utils.getText(txtArrivalTime)));
        currentTravel.setArrivalStation(Utils.getText(txtArrivalStation));
        new TravelDAO(this).update(currentTravel);
        finish();
    }
}
