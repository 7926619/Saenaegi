package com.saenaegi.lfree.SubtitleController;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class outputDataController {

    private ArrayList<SubtitleData> subtitleData=new ArrayList<>();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference ListorageReference=firebaseStorage.getReference().child( "ListenSubtitle");
    private StorageReference LostorageReference=firebaseStorage.getReference().child( "LookSubtitle");

    public void getSections(final String idvideo, List<Boolean> list){

        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals( idvideo)) {
                        Video video=snapshot.getValue(Video.class);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
