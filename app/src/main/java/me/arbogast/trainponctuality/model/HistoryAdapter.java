package me.arbogast.trainponctuality.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.arbogast.trainponctuality.gui.Utils;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 11/01/17.
 * Adapter to show Travels History list
 */

public class HistoryAdapter extends BaseAdapter {
    // View Type for Separators
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 0;
    // View Type for Regular rows
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;

    private final Context context;
    private final List<History> objects;

    public HistoryAdapter(Context context, List<History> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position).isSection()) {
            return ITEM_VIEW_TYPE_SEPARATOR;
        } else {
            return ITEM_VIEW_TYPE_REGULAR;
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History line = objects.get(position);
        if (line == null)
            return convertView;

        int itemViewType = getItemViewType(position);

        View view;
        //if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR)
                view = inflater.inflate(R.layout.show_history_section_header, null);
            else
                view = inflater.inflate(R.layout.show_history_row, null);
        }

        if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
            TextView separatorView = (TextView) view.findViewById(R.id.txtSectionHeader);

            separatorView.setText(line.getDayTravel());
        } else {
            ImageView txtLine = (ImageView) view.findViewById(R.id.imgLine);
            TextView txtMission = (TextView) view.findViewById(R.id.txtMission);
            TextView txtDepartureDate = (TextView) view.findViewById(R.id.txtDepartureDate);
            TextView txtDepartureStation = (TextView) view.findViewById(R.id.txtDepartureStation);
            TextView txtArrivalDate = (TextView) view.findViewById(R.id.txtArrivalDate);
            TextView txtArrivalStation = (TextView) view.findViewById(R.id.txtArrivalStation);

            txtLine.setImageResource(context.getResources().getIdentifier(line.getTravel().getLine().toLowerCase(), "drawable", context.getPackageName()));
            txtMission.setText(line.getTravel().getMissionCode());
            txtDepartureDate.setText(Utils.timeToString(line.getTravel().getDepartureDate()));
            txtDepartureStation.setText(line.getDepartureStation());
            if (line.getArrivalStation() != null) {
                txtArrivalDate.setText(Utils.timeToString(line.getTravel().getArrivalDate()));
                txtArrivalStation.setText(line.getArrivalStation());
            } else {
                txtArrivalDate.setVisibility(View.GONE);
                txtArrivalStation.setVisibility(View.GONE);
                TextView txtTravelInProgress = (TextView) view.findViewById(R.id.txtTravelInProgress);
                txtTravelInProgress.setVisibility(View.VISIBLE);
            }

            if (line.getIsSelected())
                view.setBackgroundResource(R.color.colorSelectedItem);
        }

        return view;
    }
}
