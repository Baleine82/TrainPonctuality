package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.HistoryAdapter;
import me.arbogast.trainponctuality.R;

public class ShowHistoryActivity extends Activity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_show_list);
        TextView t = (TextView) findViewById(R.id.txtHeaderList);
        t.setText(extras.getString("title"));
        t.setBackgroundResource(extras.getInt("color"));
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            ArrayList<History> data = dbTravel.selectHistory();


            // Detecting when the list changes day
            // I don't think that's very beautiful, but it comes from http://codetheory.in/android-dividing-listview-sections-group-headers/
            String currentDay = null;
            for (int i = 0; i < data.size(); i++) {
                History obj = data.get(i);
                if (obj == null)
                    continue;

                if (currentDay == null || !obj.getDayTravel().equals(currentDay)) {
                    currentDay = obj.getDayTravel();
                    data.add(i, new History(currentDay));
                    i++; // we skip next item, it's the item we were on
                }
            }


            HistoryAdapter adapter = new HistoryAdapter(this, data);

            listView1 = (ListView) findViewById(R.id.listView1);

            listView1.setAdapter(adapter);
        }
    }
}
