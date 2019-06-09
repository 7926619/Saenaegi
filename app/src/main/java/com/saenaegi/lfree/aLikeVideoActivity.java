package com.saenaegi.lfree;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;

import java.util.ArrayList;

public class aLikeVideoActivity extends AppCompatActivity {
    private ListView listView;
    private aListviewAdapter adapter;
    private ArrayList<aListviewItem> data = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" );
    private ArrayList<Video> videos=new ArrayList<>();
    private ArrayList<String> lvideos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_like_video);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);
        listView.setAdapter(adapter);
        getData();
    }
    public void getData() {
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvideos.clear();
                videos.clear();
                data.clear();
                for(DataSnapshot snapshot:dataSnapshot.child( "LIKEVIDEO" ).getChildren()){
                    if(snapshot.getKey().equals( "userid" )){
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            lvideos.add(snapshot1.getKey());
                        }
                    }
                }
                for(DataSnapshot snapshot:dataSnapshot.child( "VIDEO" ).getChildren()){
                    for(String idvideo:lvideos){
                        if(snapshot.getKey().equals( idvideo )){
                            Video video=snapshot.getValue(Video.class);
                            videos.add( video );
                            aListviewItem aListviewItem=new aListviewItem( video.getTitle());
                            data.add( aListviewItem );
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
