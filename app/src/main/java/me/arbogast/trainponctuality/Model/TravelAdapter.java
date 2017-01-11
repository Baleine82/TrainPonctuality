package me.arbogast.trainponctuality.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.arbogast.trainponctuality.GUI.ShowHistoryActivity;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 11/01/17.
 */

public class TravelAdapter extends ArrayAdapter<Travel> {
    private final Context context;
    private final int resource;
    private final List<Travel> objects;

    public TravelAdapter(Context context, int resource, List<Travel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TravelHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((ShowHistoryActivity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new TravelHolder();
            holder.txtId = (TextView)row.findViewById(R.id.txtId);
            holder.txtLine = (TextView)row.findViewById(R.id.txtLine);
            holder.txtMission = (TextView)row.findViewById(R.id.txtMission);
            holder.txtDepartureDate = (TextView)row.findViewById(R.id.txtDepartureDate);
            holder.txtDepartureStation = (TextView)row.findViewById(R.id.txtDepartureStation);
            holder.txtArrivalDate = (TextView)row.findViewById(R.id.txtArrivalDate);
            holder.txtArrivalStation = (TextView)row.findViewById(R.id.txtArrivalStation);

            row.setTag(holder);
        }
        else
        {
            holder = (TravelHolder)row.getTag();
        }

        Travel t = this.objects.get(position);
        holder.txtId.setText(Long.toString(t.getId()));
        holder.txtLine.setText(t.getLine());
        holder.txtMission.setText(t.getMissionCode());
        holder.txtDepartureDate.setText(t.getDepartureDate().toString());
        holder.txtDepartureStation.setText(t.getDepartureStation());
        holder.txtArrivalDate.setText(t.getArrivalDate().toString());
        holder.txtArrivalStation.setText(t.getArrivalStation());

        return row;
    }

    static class TravelHolder
    {
        TextView txtId;
        TextView txtLine;
        TextView txtMission;
        TextView txtDepartureDate;
        TextView txtArrivalDate;
        TextView txtDepartureStation;
        TextView txtArrivalStation;
    }
}
