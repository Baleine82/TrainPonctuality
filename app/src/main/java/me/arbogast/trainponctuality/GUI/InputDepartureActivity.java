package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Stops;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class InputDepartureActivity extends Activity {

    private EditText txtDepartureDate;
    private EditText txtDepartureTime;
    private TextView txtDepartureStation;
    private EditText txtLine;
    private EditText txtMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_departure);

        txtDepartureDate = (EditText) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (EditText) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (TextView) findViewById(R.id.txtLocation);
        txtLine = (EditText) findViewById(R.id.txtLine);
        txtMission = (EditText) findViewById(R.id.txtMission);

        txtDepartureDate.setText(Utils.getDate());
        txtDepartureTime.setText(Utils.getTime());
    }

    public void ValidateDeparture(View view) {
        Travel departure = new Travel(Utils.parseDate(Utils.getText(txtDepartureDate), Utils.getText(txtDepartureTime)),
                Utils.getText(txtLine), Utils.getText(txtMission), txtDepartureStation.toString());

        new TravelDAO(this).insert(departure);
        finish();
    }

    public void CancelInputDeparture(View view) {
        finish();
    }

    public void showStationList(View view) {
        Intent showList = new Intent(this, ShowStationListActivity.class);
        startActivityForResult(showList, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Stops selected = data.getExtras().getParcelable("stop");
            txtDepartureStation.setText(selected.getName());
        }
    }
}
