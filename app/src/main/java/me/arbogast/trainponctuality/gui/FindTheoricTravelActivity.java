package me.arbogast.trainponctuality.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.dbaccess.TripsDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.HistoryAdapter;
import me.arbogast.trainponctuality.model.Stops;

public class FindTheoricTravelActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<History> theoricTravels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_list);

        Bundle data = getIntent().getExtras();
        String line = data.getString("line");
        String missionCode = data.getString("mission");
        Stops departureStation = data.getParcelable("departureStation");
        Date departureDate = Utils.millisToDate(data.getLong("departureDate"));

        try (TripsDAO dbTrips = new TripsDAO(this)) {
            theoricTravels = dbTrips.findMatchingTrips(line, missionCode, departureStation, departureDate);
        }

        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.show_history_row, theoricTravels);
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("trip",theoricTravels.get(position).getTravel().getTripId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
