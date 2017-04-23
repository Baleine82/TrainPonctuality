package me.arbogast.trainponctuality.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.dbaccess.TripsDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.HistoryAdapter;
import me.arbogast.trainponctuality.model.Stops;

public class FindTheoricTravelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_list);

        Bundle data = getIntent().getExtras();
        String line = data.getString("line");
        String missionCode = data.getString("mission");
        Stops departureStation = data.getParcelable("departureStation");
        Date departureDate = Utils.millisToDate(data.getLong("departureDate"));

        List<History> result;
        try (TripsDAO dbTrips = new TripsDAO(this)) {
            result = dbTrips.findMatchingTrips(line, missionCode, departureStation, departureDate);
        }

        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.show_history_row, result);
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);
    }
}
