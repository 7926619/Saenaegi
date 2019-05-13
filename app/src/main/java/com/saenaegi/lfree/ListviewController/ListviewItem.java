package com.saenaegi.lfree.ListviewController;

import android.graphics.Bitmap;

public class ListviewItem {
    private Bitmap bit;
    private int icon;
    private String name;
    private String sub;

    public ListviewItem(String name, String sub) {
        this.icon = 0;
        this.name = name;
        this.sub = sub;
    }

    public ListviewItem(int icon, String name, String sub) {
        this.icon = icon;
        this.name = name;
        this.sub = sub;
    }

    public ListviewItem(Bitmap bit, String name, String sub){
        this.bit = bit;
        this.name = name;
        this.sub = sub;
    }

    public int getIcon() { return icon; }
    public String getName() { return name; }
    public String getSub() { return sub; }
    public Bitmap getBit() { return bit; }
}