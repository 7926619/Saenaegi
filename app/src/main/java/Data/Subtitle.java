package Data;

import java.util.TimerTask;

public class Subtitle {

    private String idsubtitle;
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
        this("-1", "direcctory", "example","example","example",0,"00:00","00:00", 0, true);
    }

    public Subtitle(String idsubtitle, String directory, String idgoogle, String idvideo, String name, int recommend, String sectionF, String sectionS, int sectionNum, boolean type){
        this.idsubtitle=idsubtitle;
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
    public String getIdsubtitle() {
        return idsubtitle;
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
