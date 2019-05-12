package com.saenaegi.lfree.Data;

public class User{
    private String idgoogle;
    private String username;
    private boolean type;
    public User(){

    }

    public User(String idgoogle, String username, boolean type){
        this.idgoogle=idgoogle;
        this.username=username;
        this.type=type;
    }

    public String getIdgoogle() {
        return idgoogle;
    }
    public String getUsername(){
        return username;
    }
    public boolean isType(){
        return type;
    }

    public void setIdgoogle(String idgoogle) {
        this.idgoogle = idgoogle;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
