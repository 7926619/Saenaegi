package com.saenaegi.lfree.Data;

public class Request {
    private String idgoogle;
    private boolean type;
    private String link;

    public Request(){

    }
    public Request(String idgoogle, boolean type, String link){
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

    public void setType(boolean type) {
        this.type = type;
    }

    public String getIdgoogle() {
        return idgoogle;
    }

    public String seeType(){
        if(type){
            return "청각 자료 요청";
        }
        else{
            return "시각 자료 요청";
        }
    }
    public boolean getType() {
        return type;
    }

    public String getLink() {
        return link;
    }
}
