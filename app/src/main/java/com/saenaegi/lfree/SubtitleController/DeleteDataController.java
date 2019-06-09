package com.saenaegi.lfree.SubtitleController;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteDataController {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" );

    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference ListorageReference=firebaseStorage.getReference().child( "ListenSubtitle");
    private StorageReference LostorageReference=firebaseStorage.getReference().child( "LookSubtitle");


    private String idvideo;
    private String idsubtitle;
    private int sectionNum;
    private boolean type;

    public DeleteDataController(String idvideo,String idsubtitle,int sectionNum,boolean type){
        this.idvideo=idvideo;
        this.idsubtitle=idsubtitle;
        this.sectionNum=sectionNum;
        this.type=type;
    }

    public void deleteData(){
        DatabaseReference dataRef2=databaseReference.child( "VIDEO" );
        dataRef2.child( idvideo ).child( "SUBTITLE" ).child( String.valueOf(sectionNum) ).child( idsubtitle ).removeValue();

        if(type){
            ListorageReference.child( idvideo ).child( String.valueOf(sectionNum) ).child( idsubtitle +".txt").delete();
        }
        else{
            LostorageReference.child( idvideo).child( String.valueOf( sectionNum )).child( idsubtitle +".txt").delete();
        }
    }
}
