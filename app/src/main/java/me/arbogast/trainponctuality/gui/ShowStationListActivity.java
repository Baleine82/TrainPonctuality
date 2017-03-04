package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.arbogast.trainponctuality.model.Stops;
import me.arbogast.trainponctuality.model.StopsAdapter;
import me.arbogast.trainponctuality.R;

public class ShowStationListActivity extends AppCompatActivity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        ArrayList<Stops> stops = extras.getParcelableArrayList("stops");
        setContentView(R.layout.activity_show_list);
        setTitle(extras.getInt("title"));

        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, extras.getInt("color"))));


        // Get ListView object from xml
        listView1 = (ListView) findViewById(R.id.listView1);

        StopsAdapter adapter = new StopsAdapter(this, R.layout.show_simple_list, stops);

        // Assign adapter to ListView
        listView1.setAdapter(adapter);

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
