package com.saenaegi.lfree.ListviewController;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saenaegi.lfree.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        aListviewItem alistviewitem = data.get(position);

        TextView name = (TextView)convertView.findViewById(R.id.title);

        if(getCount() >= 2) {
            TextView namebefore = (TextView)convertView.findViewWithTag(getItemId(position-1));
            Log.e(TAG, "NameBefore : " + namebefore);
            name.setAccessibilityTraversalBefore(namebefore.getId());
        }

        name.setText(alistviewitem.getName());
        name.setTag(getItemId(position));
        //name.setId((int)getItemId(position));
        name.setContentDescription(alistviewitem.getName());
        name.setFocusable(true);
        name.setFocusableInTouchMode(true);
        //name.setAccessibilityTraversalBefore();
        //name.setAccessibilityTraversalAfter();
        Log.e(TAG, "=========================================================================");
        Log.e(TAG, "ListView Text : " + name.getText());
        Log.e(TAG, "ListView Tag : " + name.getTag());
        Log.e(TAG, "ListView id : " + name.getId());
        Log.e(TAG, "ListView ItemId : " + getItemId(position));
        Log.e(TAG, "ListView ContentDescription : " + name.getContentDescription());
        Log.e(TAG, "=========================================================================");

        return convertView;
    }
}
