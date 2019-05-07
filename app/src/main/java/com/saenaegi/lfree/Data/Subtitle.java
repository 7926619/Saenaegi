package com.saenaegi.lfree.Data;

public class Subtitle {

    private String directory;
    private String idgoogle;
    private String idvideo;
    private String name;
    private int recommend;
    private String sectionF;
    private int sectionNum;
    private String sectionS;
    private boolean type;

    public Subtitle(){

    }

    public Subtitle(String directory, String idgoogle, String idvideo, String name, int recommend, String sectionF, String sectionS, int sectionNum, boolean type){
        this.idgoogle=idgoogle;
        this.directory=directory;
        this.idvideo=idvideo;
        this.name=name;
        this.recommend=recommend;
        this.sectionF=sectionF;
        this.sectionS=sectionS;
        this.sectionNum=sectionNum;
        this.type=type;

    }

    public String getDirectory() {
        return directory;
    }

    public String getIdgoogle() {
        return idgoogle;
    }

    public String getIdvideo() {
        return idvideo;
    }

    public String getName() {
        return name;
    }

    public int getRecommend() {
        return recommend;
    }

    public int getSectionNum() {
        return sectionNum;
    }

    public String getSectionF() {
        return sectionF;
    }

    public String getSectionS() {
        return sectionS;
    }

    public boolean isType() {
        return type;
    }
}
