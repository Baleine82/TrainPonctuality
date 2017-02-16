package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.arbogast.trainponctuality.dbaccess.TravelDAO;
import me.arbogast.trainponctuality.model.History;
import me.arbogast.trainponctuality.model.HistoryAdapter;
import me.arbogast.trainponctuality.R;

public class ShowHistoryActivity extends Activity implements AdapterView.OnItemLongClickListener {
    private ListView listView1;
    private ArrayList<History> data;
    private ShowHistoryActivity activityThis;
    private HistoryAdapter adapter;
    private int clickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityThis = this;

        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_show_list);
        TextView t = (TextView) findViewById(R.id.txtHeaderList);
        t.setText(extras.getString("title"));
        t.setBackgroundResource(extras.getInt("color"));
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
            listView1 = (ListView) findViewById(R.id.listView1);
            listView1.setAdapter(adapter);
            listView1.setOnItemLongClickListener(this);
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (data.get(position).isSection())
            return false;

        clickedPosition = position;

        View txtDelete = view.findViewById(R.id.txtDeleteLine);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setVisibility(View.VISIBLE);
        txtDelete.setVisibility(View.VISIBLE);
        return true;
    }

    public void DeleteHistoryLine(View view) {
        History requestDeletion = data.get(clickedPosition);
        try (TravelDAO dbTravel = new TravelDAO(activityThis)) {
            dbTravel.delete(Long.parseLong(requestDeletion.getId()));
            data.remove(clickedPosition);
            adapter.notifyDataSetChanged();
        }
    }
}
