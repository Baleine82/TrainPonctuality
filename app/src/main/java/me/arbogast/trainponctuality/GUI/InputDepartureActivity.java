package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import me.arbogast.trainponctuality.DBAccess.RoutesDAO;
import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.Line;
import me.arbogast.trainponctuality.Model.LineAdapter;
import me.arbogast.trainponctuality.Model.Stops;
import me.arbogast.trainponctuality.Model.Travel;
import me.arbogast.trainponctuality.R;

public class InputDepartureActivity extends Activity {
    private static final int RESULT_GET_DEPARTURE_STATION = 0;
    private EditText txtDepartureDate;
    private EditText txtDepartureTime;
    private TextView txtDepartureStation;
    private Spinner spnLine;
    private EditText txtMission;

    private Stops DepartureStation;
    private Line selectedLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_departure);

        txtDepartureDate = (EditText) findViewById(R.id.txtDepartureDate);
        txtDepartureTime = (EditText) findViewById(R.id.txtDepartureTime);
        txtDepartureStation = (TextView) findViewById(R.id.txtLocation);
        spnLine = (Spinner) findViewById(R.id.spnLine);
        txtMission = (EditText) findViewById(R.id.txtMission);

        txtDepartureDate.setText(Utils.getDate());
        txtDepartureTime.setText(Utils.getTime());

        spnLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLine = (Line) spnLine.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLine = null;
            }
        });

        LineAdapter adapter = new LineAdapter(this, R.layout.spinner_line_layout, new RoutesDAO(this).getDistinctLines());
        spnLine.setAdapter(adapter);
    }

    public void ValidateDeparture(View view) {
        if (DepartureStation == null)
            return;
        Travel departure = new Travel(Utils.parseDate(Utils.getText(txtDepartureDate), Utils.getText(txtDepartureTime)),
                ((Line) spnLine.getSelectedItem()).getCode(), Utils.getText(txtMission), DepartureStation.getId());

        new TravelDAO(this).insert(departure);
        finish();
    }

    public void CancelInputDeparture(View view) {
        finish();
    }

    public void showStationList(View view) {
        Intent showList = new Intent(this, ShowStationListActivity.class);
        showList.putExtra("line", selectedLine.getCode());
        startActivityForResult(showList, RESULT_GET_DEPARTURE_STATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_GET_DEPARTURE_STATION && resultCode == RESULT_OK && data != null) {
            Stops selected = data.getExtras().getParcelable("stop");
            DepartureStation = selected;
            txtDepartureStation.setText(selected.getName());
        }
    }
}
