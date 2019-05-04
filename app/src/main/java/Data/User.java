package Data;

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
}
