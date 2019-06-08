package com.saenaegi.lfree.ListviewController;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saenaegi.lfree.R;

import java.util.ArrayList;

public class aListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<aListviewItem> data;
    private int layout;

    public aListviewAdapter(Context context, int layout, ArrayList<aListviewItem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public String getItem(int position) { return data.get(position).getName(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        //convertView = inflater.inflate(layout, parent, false);

        aListviewItem alistviewitem = data.get(position);

        alistviewitem.setId((int)getItemId(position));
        alistviewitem.setTag((int)getItemId(position));

        TextView name = (TextView)convertView.findViewById(R.id.title);

        name.setText(alistviewitem.getName());
        name.setTag(alistviewitem.getTag());
        name.setContentDescription(alistviewitem.getName());
        name.setFocusable(true);
        name.setFocusableInTouchMode(true);

        return convertView;
    }
}
