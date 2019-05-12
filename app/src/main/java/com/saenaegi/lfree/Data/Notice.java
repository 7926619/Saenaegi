package com.saenaegi.lfree.Data;

public class Notice {
    private String text;
    private String subtitle;
    private String time;
    private int view;

    public Notice(){
    }

    public Notice(String text, String subtitle, String time, int view){
        this.text=text;
        this.subtitle=subtitle;
        this.time=time;
        this.view=view;
    }

    public int getView() {
        return view;
    }
    public String getText(){
        return text;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTime() {
        return time;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setView(int view) {
        this.view = view;
    }
}
