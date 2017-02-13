package me.arbogast.trainponctuality.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.arbogast.trainponctuality.gui.ShowStationListActivity;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 11/01/17.
 * Adapter to show stops in a list
 */

public class StopsAdapter extends ArrayAdapter<Stops> {
    private final Context context;
    private final int resource;
    private final List<Stops> objects;

    public StopsAdapter(Context context, int resource, List<Stops> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        StopsHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((ShowStationListActivity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new StopsHolder();
            //holder.txtId = (TextView)row.findViewById(R.id.txtId);
            holder.txtName = (TextView)row.findViewById(R.id.txtElem);

            row.setTag(holder);
        }
        else
        {
            holder = (StopsHolder)row.getTag();
        }

        Stops t = this.objects.get(position);
        //holder.txtId.setText(t.getId());
        holder.txtName.setText(t.getName());

        return row;
    }

    private class StopsHolder
    {
        TextView txtId;
        TextView txtName;
    }
}
