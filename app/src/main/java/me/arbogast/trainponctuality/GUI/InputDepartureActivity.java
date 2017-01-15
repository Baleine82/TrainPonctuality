package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class InputDepartureActivity extends Activity {

    private EditText txtDepartureDate;
    private EditText txtDepartureTime;
    private EditText txtDepartureStation;
    private EditText txtLine;
    private EditText txtMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_departure);

        txtDepartureDate = (EditText) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (EditText) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (EditText) findViewById(R.id.txtLocation);
        txtLine = (EditText) findViewById(R.id.txtLine);
        txtMission = (EditText) findViewById(R.id.txtMission);

        txtDepartureDate.setText(Utils.getDate());
        txtDepartureTime.setText(Utils.getTime());
    }

    public void ValidateDeparture(View view) {
        Travel departure = new Travel(Utils.parseDate(Utils.getText(txtDepartureDate), Utils.getText(txtDepartureTime)),
                Utils.getText(txtLine), Utils.getText(txtMission), Utils.getText(txtDepartureStation));

        new TravelDAO(this).insert(departure);
        finish();
    }

    public void CancelInputDeparture(View view) {
        finish();
    }
}
