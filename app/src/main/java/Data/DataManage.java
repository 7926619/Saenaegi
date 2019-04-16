package Data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataManage implements DataManager{
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();

    @Override
    public boolean getUserQuery(Data data) {
        return false;
    }

    @Override
    public boolean setUserQuery(String id, String user, boolean see) {
        User utemp=new User(id,user,see);
        databaseReference.child("LFREE").child("USER").child(id).setValue(utemp);
        return true;
    }
}
