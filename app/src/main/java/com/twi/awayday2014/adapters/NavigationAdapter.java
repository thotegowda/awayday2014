package com.twi.awayday2014.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twi.awayday2014.R;


public class NavigationAdapter extends BaseAdapter {

    private Context context;
    private String[] values;
    private Drawable[] icons;
    private LayoutInflater inflater;

    public NavigationAdapter(Context context, String[] values, Drawable[] icons) {
        this.context = context;
        this.values = values;
        this.icons = icons;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView navigationItem = (TextView) view;
        if (navigationItem == null) {
            navigationItem = (TextView) inflater.inflate(R.layout.navigation_item, viewGroup, false);
        }

        navigationItem.setText(values[i]);
        //navigationItem.setCompoundDrawables(icons[i], null, null, null);
        return navigationItem;
    }
}
