package me.arbogast.trainponctuality.gui;

import android.content.Intent;
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

        adapter = new HistoryAdapter(this, R.layout.show_history_row, data);
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
        }

        data.remove(selectedItem);
        adapter.notifyDataSetChanged();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        History selected = data.get(position);
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

    public void ModifyHistoryLine(MenuItem item) {
        Intent editTravel = new Intent(this, EditTravelActivity.class);
        editTravel.putExtra("history", selectedItem);
        startActivityForResult(editTravel, Utils.RESULT_EDIT_TRAVEL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utils.RESULT_EDIT_TRAVEL && resultCode == RESULT_OK) {
            int idxSelect = this.data.indexOf(selectedItem);
            this.data.remove(idxSelect);
            History res = data.getExtras().getParcelable("history");
            this.data.add(idxSelect,res);

            adapter.notifyDataSetChanged();
        }
    }
}
