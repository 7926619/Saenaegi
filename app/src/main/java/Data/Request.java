package Data;

public class Request {
    private String idgoogle;
    private String type;
    private String link;

    public Request(){

    }
    public Request(String idgoogle, String type, String link){
        this.idgoogle=idgoogle;
        this.type=type;
        this.link=link;
    }

    public void setIdgoogle(String idgoogle) {
        this.idgoogle = idgoogle;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdgoogle() {
        return idgoogle;
    }

    public String seeType(){
        if(type=="true"){
            return "청각 자료 요청";
        }
        else{
            return "시각 자료 요청";
        }
    }
    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }
}
