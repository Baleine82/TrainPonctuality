package me.arbogast.trainponctuality.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.arbogast.trainponctuality.gui.Utils;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 11/01/17.
 * Adapter to show Travels History list
 */

public class HistoryAdapter extends ArrayAdapter<History> {
    private final Context context;
    private final int resource;
    private final List<History> objects;

    public HistoryAdapter(Context context, int resource, List<History> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.objects = objects;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        HistoryHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new HistoryHolder();
            holder.txtLine = (ImageView) convertView.findViewById(R.id.imgLine);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtMission = (TextView) convertView.findViewById(R.id.txtMission);
            holder.txtDepartureDate = (TextView) convertView.findViewById(R.id.txtDepartureDate);
            holder.txtDepartureStation = (TextView) convertView.findViewById(R.id.txtDepartureStation);
            holder.txtArrivalDate = (TextView) convertView.findViewById(R.id.txtArrivalDate);
            holder.txtArrivalStation = (TextView) convertView.findViewById(R.id.txtArrivalStation);
            holder.txtTravelInProgress = (TextView) convertView.findViewById(R.id.txtTravelInProgress);

            convertView.setTag(holder);
        } else {
            holder = (HistoryHolder) convertView.getTag();
        }

        History line = objects.get(position);
        holder.txtDate.setText(Utils.dateToString(line.getTravel().getDepartureDate()));
        holder.txtLine.setImageResource(context.getResources().getIdentifier(line.getTravel().getLine().toLowerCase(), "drawable", context.getPackageName()));
        holder.txtMission.setText(line.getTravel().getMissionCode());
        holder.txtDepartureDate.setText(Utils.timeToString(line.getTravel().getDepartureDate()));
        holder.txtDepartureStation.setText(line.getDepartureStation());
        if (line.getArrivalStation() != null) {
            holder.txtArrivalDate.setVisibility(View.VISIBLE);
            holder.txtArrivalStation.setVisibility(View.VISIBLE);
            holder.txtTravelInProgress.setVisibility(View.GONE);
            holder.txtArrivalDate.setText(Utils.timeToString(line.getTravel().getArrivalDate()));
            holder.txtArrivalStation.setText(line.getArrivalStation());
        } else {
            holder.txtArrivalDate.setVisibility(View.GONE);
            holder.txtArrivalStation.setVisibility(View.GONE);
            holder.txtTravelInProgress.setVisibility(View.VISIBLE);
        }

        convertView.setBackgroundResource(line.getIsSelected() ? R.color.colorSelectedItem : 0);

        return convertView;
    }

    private static class HistoryHolder {
        private TextView txtArrivalDate;
        private ImageView txtLine;
        private TextView txtDate;
        private TextView txtMission;
        private TextView txtDepartureDate;
        private TextView txtDepartureStation;
        private TextView txtArrivalStation;
        private TextView txtTravelInProgress;
    }
}
