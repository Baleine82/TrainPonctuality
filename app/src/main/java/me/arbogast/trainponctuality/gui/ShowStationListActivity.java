package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.arbogast.trainponctuality.dbaccess.StopsDAO;
import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.StopsAdapter;
import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.services.LocationService;

public class ShowStationListActivity extends Activity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Location myLocation = new LocationService(this).getLastBestLocation();

        Bundle extras = getIntent().getExtras();
        String line = extras.getString("line");
        setContentView(R.layout.activity_show_list);

        // Get ListView object from xml
        listView1 = (ListView) findViewById(R.id.listView1);
        TextView t = (TextView) findViewById(R.id.txtHeaderList);

        t.setText(extras.getString("title"));
        t.setBackgroundResource(extras.getInt("color"));

        try (StopsDAO dbStops = new StopsDAO(this)) {
            List<Stops> stops = dbStops.getStopsForLine(line);

            if (myLocation != null) {
                for (Stops stop : stops)
                    stop.setDistanceFromUser(myLocation.distanceTo(stop.getLocation()));

                Collections.sort(stops, Stops.LOCATION_COMPARATOR);
            }

            StopsAdapter adapter = new StopsAdapter(this, R.layout.show_simple_list, stops);

            // Assign adapter to ListView
            listView1.setAdapter(adapter);
        }

        // ListView Item Click Listener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("stop", (Stops) listView1.getItemAtPosition(position));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
