package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.StopsAdapter;
import me.arbogast.trainponctuality.R;
import me.arbogast.trainponctuality.services.LocationProxy;

public class ShowStationListActivity extends AppCompatActivity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Location myLocation = LocationProxy.getInstance().getLastBest();
        LocationProxy.getInstance().stopRequest(this);

        Bundle extras = getIntent().getExtras();
        String line = extras.getString("line");
        setContentView(R.layout.activity_show_list);

        // Get ListView object from xml
        listView1 = (ListView) findViewById(R.id.listView1);

        GetStationForLineAsync findStationAsync = new GetStationForLineAsync() {
            @Override
            protected void onPostExecute(List<Stops> stops) {
                super.onPostExecute(stops);
                StopsAdapter adapter = new StopsAdapter(ctx, R.layout.show_simple_list, stops);

                // Assign adapter to ListView
                listView1.setAdapter(adapter);
            }
        };

        findStationAsync.execute(new GetStationForLineParams(this, line, myLocation));

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
