package me.arbogast.trainponctuality.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 23/01/17.
 */

public class LineAdapter extends ArrayAdapter<Line> {
    private final Context context;
    private final int resource;
    private final List<Line> objects;

    public LineAdapter(Context context, int resource, List<Line> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        Line t = this.objects.get(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_line_layout, parent, false);

        ImageView imageView = (ImageView)row.findViewById(R.id.spinnerImage);
        imageView.setImageResource(context.getResources().getIdentifier(t.getCode().toLowerCase(), "drawable", context.getPackageName()));

        return row;
    }
}
