package Data;

public class Video {
    private String id;
    private String link;
    private boolean lookstate;
    private boolean listenstate;
    private int sectionCount;
    private int view;

    public Video(){

    }
    public  Video(String id, String link, boolean lookstate, boolean listenstate, int sectionCount,int view){
        this.id=id;
        this.link=link;
        this.lookstate=lookstate;
        this.listenstate=listenstate;
        this.sectionCount=sectionCount;
        this.view=view;
    }

    public String getId() {
        return id;
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
