package me.arbogast.trainponctuality.Model;

import android.content.Context;
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
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return makeLayout(position, convertView, parent, R.layout.spinner_line_layout);
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
        return makeLayout(position, convertView, parent, R.layout.spinner_line_layout);
    }

    private View makeLayout(final int position, final View convertView, final ViewGroup parent, final int layout) {
        ImageView tv;
        if (convertView != null) {
            tv = (ImageView) convertView;
        } else {
            tv = (ImageView) LayoutInflater.from(context).inflate(layout,
                    parent, false);
        }

        Line t = this.objects.get(position);
        tv.setImageResource(context.getResources().getIdentifier(t.getCode().toLowerCase(), "drawable", context.getPackageName()));

        return tv;
    }
}