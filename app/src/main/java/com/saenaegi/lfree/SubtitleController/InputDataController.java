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
import java.util.ArrayList;
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

    public void modifyData(String key,ArrayList<SubtitleData> subtitlesData, String idvideo,File filedirectory, int sectionNum,boolean type) {
        ArrayList<String> subtitles=new ArrayList<>();
        for(SubtitleData subtitleData:subtitlesData){
            subtitles.add(subtitleData.getString());
        }
        String filename=key+".txt";
        File storagefile=writeFile( subtitles, filename,filedirectory );

        Uri file=Uri.fromFile(storagefile);
        UploadTask uploadTask;
        if(type){
            uploadTask=ListorageReference.child(idvideo+"/"+sectionNum+"/"+file.getLastPathSegment()).putFile(file);
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
        }else {
            uploadTask=LostorageReference.child(idvideo+"/"+sectionNum+"/"+file.getLastPathSegment()).putFile(file);
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

    }
    public void storeData(Subtitle subtitle,ArrayList<SubtitleData> subtitlesData, String idvideo,File filedirectory, int sectionNum){

        DatabaseReference dataRef=databaseReference.child( idvideo ).child( "SUBTITLE" );

        ArrayList<String> subtitles=new ArrayList<>();
        for(SubtitleData subtitleData:subtitlesData){
            subtitles.add(subtitleData.getString());
        }
        String key=dataRef.push().getKey();
        String filename=key+".txt";
        File storagefile=writeFile( subtitles, filename,filedirectory );

        Uri file=Uri.fromFile(storagefile);
        UploadTask uploadTask;
        if(subtitle.isType()) {
            uploadTask=ListorageReference.child(idvideo+"/"+sectionNum+"/"+file.getLastPathSegment()).putFile(file);
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
            uploadTask=LostorageReference.child(idvideo+"/"+sectionNum+"/"+file.getLastPathSegment()).putFile(file);
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
        dataRef.child( String.valueOf( sectionNum) ).child( key ).setValue( subtitle );
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
            android.util.Log.d(null, "==================> " + e.toString());
        }

        if(filewrite != null){
            try{
                filewrite.close();
            }catch (IOException e){
                android.util.Log.d(null, "==================> " + e.toString());
            }
            catch (NullPointerException e) {
                android.util.Log.d(null, "==================> " + e.toString());
            }
            catch (IndexOutOfBoundsException e) {
                android.util.Log.d(null, "==================> " + e.toString());
            }
        }

        return file;
    }
}
