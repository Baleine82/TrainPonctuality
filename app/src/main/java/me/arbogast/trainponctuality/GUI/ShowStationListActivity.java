package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import me.arbogast.trainponctuality.DBAccess.StopsDAO;
import me.arbogast.trainponctuality.Model.Stops;
import me.arbogast.trainponctuality.Model.StopsAdapter;
import me.arbogast.trainponctuality.R;

public class ShowStationListActivity extends Activity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        // Get ListView object from xml
        listView1 = (ListView) findViewById(R.id.listView1);

        StopsAdapter adapter = new StopsAdapter(this, R.layout.show_simple_list, new StopsDAO(this).selectAll());


        // Assign adapter to ListView
        listView1.setAdapter(adapter);

        // ListView Item Click Listener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("stop", (Stops) listView1.getItemAtPosition(position));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
