package com.saenaegi.lfree.Data;

public class Video {
    private String link;
    private boolean lookstate;
    private boolean listenstate;
    private int sectionCount;
    private int view;

    public Video(){

    }
    public  Video(String link, boolean lookstate, boolean listenstate, int sectionCount,int view){
        this.link=link;
        this.lookstate=lookstate;
        this.listenstate=listenstate;
        this.sectionCount=sectionCount;
        this.view=view;
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
    public void setView(int view){
        this.view=view;
    }
    public String noTrueListenstate(){
        if(this.listenstate==false)
            return "청각 자료 제직중";
        else
            return "청각 자료 완성";
    }

    public String noTrueLookstate(){
        if(this.lookstate==false)
            return "시각 자료 제작중";
        else
            return "시각 자료 완성";
    }
    public String noTrueAllState(){
        return "시각 청각 둘 다 제작중";
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

    public int getSectionCount() {
        return sectionCount;
    }
}
