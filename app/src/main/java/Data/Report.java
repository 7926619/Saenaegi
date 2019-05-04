package Data;

public class Report {
    private String id;
    private String idgoogle;
    private String idreported;
    private String matter;
    private int subtitle;

    public Report(){

    }
    public Report(String id, String idgoogle, String idreported, String matter, int subtitle){
        this.id=id;
        this.idgoogle=idgoogle;
        this.idreported=idreported;
        this.matter=matter;
        this.subtitle=subtitle;
    }

    public String getId(){
        return id;
    }

    public String getIdgoogle(){
        return idgoogle;
    }

    public String getIdreported(){
        return idreported;
    }

    public String getMatter(){
        return matter;
    }

    public int getSubtitle(){
        return subtitle;
    }
}
