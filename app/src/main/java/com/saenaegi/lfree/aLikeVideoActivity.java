package com.saenaegi.lfree;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.LIkevideo;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.ListviewItem;

import java.util.ArrayList;
import java.util.HashMap;

public class aLikeVideoActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "LIKEVIDEO" );
    private DatabaseReference vdatabaseRefereence=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private HashMap<String, Video> videos=new HashMap<>();
    private ArrayList<Video> lvideos=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_like_video);
        getData();
    }
    public void getData(){
        vdatabaseRefereence.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                    videos.put( snapshot.getKey(),video );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvideos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    if(snapshot.getKey().equals( "userid" )){
                        for(DataSnapshot temp:snapshot.getChildren()){
                            LIkevideo likeVideo=temp.getValue(LIkevideo.class);
                            Video video=videos.get(likeVideo.getIdvideo());
                            lvideos.add( video );
                            ListviewItem listviewItem = new ListviewItem(R.drawable.icon,video.getLink(), String.valueOf(video.getView()));
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
