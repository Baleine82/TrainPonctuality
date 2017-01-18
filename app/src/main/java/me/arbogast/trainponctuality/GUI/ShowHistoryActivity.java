package me.arbogast.trainponctuality.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import me.arbogast.trainponctuality.DBAccess.TravelDAO;
import me.arbogast.trainponctuality.Model.TravelAdapter;
import me.arbogast.trainponctuality.R;

public class ShowHistoryActivity extends Activity {
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        TravelAdapter adapter = new TravelAdapter(this, R.layout.show_history_row, new TravelDAO(this).selectAll());


        listView1 = (ListView)findViewById(R.id.listView1);

//        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
//        listView1.addHeaderView(header);

        listView1.setAdapter(adapter);
    }
}
