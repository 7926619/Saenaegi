package com.saenaegi.lfree.Data;

import android.graphics.Bitmap;

public class Video {
    private String link;
    private String title;
    private String bitt;
    private boolean lookstate;
    private boolean listenstate;
    private int sectionCount;
    private int view;

    public Video(){

    }
    public  Video(String link, String title, boolean lookstate, boolean listenstate, int sectionCount,int view, String bitt){
        this.link=link;
        this.title=title;
        this.lookstate=lookstate;
        this.listenstate=listenstate;
        this.sectionCount=sectionCount;
        this.view=view;
        this.bitt=bitt;
    }

    public void setLink(String link){
        this.link=link;
    }

    public void setLookstate(boolean lookstate){
        this.lookstate=lookstate;
    }

    public void setListenstate(boolean listenstate){
        this.listenstate=listenstate;
    }

    public void setSectionCount(int sectionCount){
        this.sectionCount=sectionCount;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setView(int view){
        this.view=view;
    }

    public String getLink() {
        return link;
    }

    public boolean isLookstate() {
        return lookstate;
    }

    public boolean isListenstate() {
        return listenstate;
    }

    public int getView() {
        return view;
    }

    public String getTitle() {
        return title;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public String getBitt() {
        return bitt;
    }

    public void setBitt(String bitt) {
        this.bitt = bitt;
    }
}
