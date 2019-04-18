package Data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataManage implements DataManager{
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();

    @Override
    public boolean getUserQuery(User user) {
        return false;
    }

    @Override
    public boolean setUserQuery(String id, String username, boolean see) {
        User user=new User(id,username,see);
        databaseReference.child("LFREE").child("USER").child(id).setValue(user);
        return true;
    }

    @Override
    public boolean getReportQuery(Report report) {
        return false;
    }

    @Override
    public boolean setReportQuery(String id, String idgoogle, String idreported, String matter, int subtitle) {
        Report report=new Report(id,idgoogle,idreported,matter, subtitle);
        databaseReference.child("LFREE").child("REPORT").child(id).setValue(report);
        return true;
    }

    @Override
    public boolean getRequestQuery(Request request) {
        return false;
    }

    @Override
    public boolean setRequestQuery(String id, String idgoogle, boolean type, String link) {
        Request request=new Request(id,idgoogle,type,link);
        databaseReference.child("LFREE").child("REQUEST").setValue(request);
        return true;
    }

    @Override
    public boolean getVideoQuery(Video video) {
        return false;
    }

    @Override
    public boolean setVideoQuery(String id, String link, boolean lookstate, boolean listenstate, int sectionCount, int view) {
        Video video=new Video(id,link,lookstate,listenstate,sectionCount,view);
        databaseReference.child("LFREE").child("VIDEO").setValue(video);
        return true;
    }

}
