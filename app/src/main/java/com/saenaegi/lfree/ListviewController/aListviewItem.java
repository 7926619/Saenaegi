package com.saenaegi.lfree.ListviewController;

public class aListviewItem {

    private String name;
    private int id;
    private int tag;

    public aListviewItem(String name) {
        this.name = name;
    }
    public String getName() { return name; }
    public int getId() { return id; }
    public int getTag() { return tag; }

    public void setId(int id) { this.id = id; }
    public void setTag(int tag) { this.tag = tag; }
}
