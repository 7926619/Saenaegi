package Data;

public class Request {
    private String id;
    private String idgoogle;
    private boolean type;
    private String link;

    public Request(){
        this("no","example",true,"example");
    }
    public Request(String id, String idgoogle, boolean type, String link){
        this.id=id;
        this.idgoogle=idgoogle;
        this.type=type;
        this.link=link;
    }

    public String getId() {
        return id;
    }

    public String getIdgoogle() {
        return idgoogle;
    }

    public boolean isType() {
        return type;
    }

    public String getLink() {
        return link;
    }
}
