package me.arbogast.trainponctuality.gui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    private History selectedItem;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.titleHistory);

        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.historyHeader)));

        //Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_show_list);

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            data = dbTravel.selectHistory();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (selectedItem != null)
            getMenuInflater().inflate(R.menu.menu_history, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void DeleteHistoryLine(MenuItem item) {
        if (selectedItem == null)
            return;

        try (TravelDAO dbTravel = new TravelDAO(this)) {
            dbTravel.delete(Long.parseLong(selectedItem.getTravel().getId()));

            String dayOfTravel = selectedItem.getDayTravel();
            int indexRemove = data.indexOf(selectedItem);
            // remove associated header if it was the only travel for the date
            if ((indexRemove == data.size() - 1) || !data.get(indexRemove + 1).getDayTravel().equals(dayOfTravel) &&
                    (indexRemove < 2 || !data.get(indexRemove - 2).getDayTravel().equals(dayOfTravel)))
                data.remove(indexRemove - 1);
            data.remove(selectedItem);

            adapter.notifyDataSetChanged();
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        History selected = data.get(position);
        if (selected.isSection())
            return false;

        setSelectedItem(selected);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History selected = data.get(position);
        if (selected == selectedItem)
            return;

        setSelectedItem(null);
    }

    private void setSelectedItem(History chosen) {
        if (selectedItem == chosen)
            return;

        if (selectedItem != null)
            selectedItem.setIsSelected(false);

        if (chosen != null)
            chosen.setIsSelected(true);

        selectedItem = chosen;
        adapter.notifyDataSetChanged();
        supportInvalidateOptionsMenu();
    }
}
