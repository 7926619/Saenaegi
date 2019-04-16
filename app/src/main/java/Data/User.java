package Data;

public class User implements Data{
    public String idgoogle;
    public String username;
    public boolean see;

    public User(){
        this("example","example",true);
    }
    public User(String idgoogle, String username, boolean see){
        this.idgoogle=idgoogle;
        this.username=username;
        this.see=see;
    }

    @Override
    public String getKey() {
        return idgoogle;
    }
}
