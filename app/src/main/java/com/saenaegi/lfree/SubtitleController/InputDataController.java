package com.saenaegi.lfree.SubtitleController;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saenaegi.lfree.Data.Subtitle;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import java.io.File;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


public class InputDataController {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference ListorageReference=firebaseStorage.getReference().child( "ListenSubtitle");
    private StorageReference LostorageReference=firebaseStorage.getReference().child( "LookSubtitle");
    private final String Listen_URL="gs://lfree-c8021.appspot.com/ListenSubtitle";
    private final String Look_URL="gs://lfree-c8021.appspot.com/LookSubtitle";

    public void storeData(Subtitle subtitle,ArrayList<String> subtitles, String idvideo,File filedirectory){

        DatabaseReference dataRef=databaseReference.child( idvideo ).child( "SUBTITLE" );

        String key=dataRef.push().getKey();
        String filename=key+".txt";
        File storagefile=writeFile( subtitles, filename,filedirectory );

        Uri file=Uri.fromFile(storagefile);
        UploadTask uploadTask;
        if(subtitle.isType()) {
            uploadTask=ListorageReference.child(idvideo+"/"+subtitle.getSectionNum()+"/"+file.getLastPathSegment()).putFile(file);
            uploadTask.addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("errorInput","false");
                }
            } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("errorInput","success");
                }
            } );
        }
        else {
            uploadTask=LostorageReference.child( idvideo ).putFile(file);
            uploadTask.addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("errorInput","false");
                }
            } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("errorInput","success");
                }
            } );
        }
        dataRef.child( String.valueOf( subtitle.getSectionNum() ) ).child( key ).setValue( subtitle );
    }

    public File writeFile(ArrayList<String> subtitle, String filename, File filedirectory) {

        FileWriter filewrite=null;
        File file=null;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : subtitle) {
            stringBuilder.append( string );
        }

        try {

            file = new File( filedirectory.getAbsoluteFile(), filename );
            filewrite=new FileWriter( file );
            filewrite.write( stringBuilder.toString() );

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(filewrite != null){

            try{
                filewrite.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return file;
    }
}
