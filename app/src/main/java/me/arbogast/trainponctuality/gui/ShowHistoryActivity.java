package me.arbogast.trainponctuality.gui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.HistoryAdapter;
import me.arbogast.trainponctuality.R;

public class ShowHistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ArrayList<History> data;
    private int clickedPosition = -1;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_show_list);

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            data = dbTravel.selectHistory();

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

            adapter = new HistoryAdapter(this, data);
            ListView listView1 = (ListView) findViewById(R.id.listView1);
            listView1.setAdapter(adapter);
            listView1.setOnItemLongClickListener(this);
            listView1.setOnItemClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (clickedPosition > -1)
            getMenuInflater().inflate(R.menu.menu_history, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void DeleteHistoryLine(MenuItem item) {
        if (clickedPosition < 0)
            return;

        History requestDeletion = data.get(clickedPosition);
        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.delete(Long.parseLong(requestDeletion.getId()));
            data.remove(clickedPosition);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        try {
            if (data.get(position).isSection())
                return false;

            clickedPosition = position;
            view.setBackgroundResource(R.color.colorSelectedItem);
            return true;
        } finally {
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(clickedPosition<0)
            return;
        parent.getChildAt(clickedPosition).setBackgroundColor(Color.TRANSPARENT);
        clickedPosition = -1;
        supportInvalidateOptionsMenu();
    }
}
