package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Stops;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class InputArrivalActivity extends Activity {
    private static final int RESULT_GET_DEPARTURE_STATION = 0;
    private EditText txtArrivalDate;
    private EditText txtArrivalTime;
    private TextView txtArrivalStation;

    private Travel currentTravel;

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
    }

    public void CancelInputArrival(View view) {
        finish();
    }

    public void ValidateArrival(View view) {
        currentTravel.setArrivalDate(Utils.parseDate(Utils.getText(txtArrivalDate), Utils.getText(txtArrivalTime)));
        new TravelDAO(this).update(currentTravel);
        finish();
    }

    public void showStationList(View view) {
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("title",getString(R.string.txtLocationArrivalHint));
        showList.putExtra("color", R.color.stationSelection);
        showList.putExtra("line", currentTravel.getLine());
        startActivityForResult(showList, RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_GET_DEPARTURE_STATION && resultCode == RESULT_OK && data != null) {
            Stops selected = data.getExtras().getParcelable("stop");
            if (selected == null)
                return;
            currentTravel.setArrivalStation(selected.getId());
            txtArrivalStation.setText(selected.getName());
        }
    }
}
