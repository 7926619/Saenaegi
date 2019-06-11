package com.saenaegi.lfree.SubtitleController;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class outputDataController {

    private ArrayList<SubtitleData> allOfSubtitle=new ArrayList<>();
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference LookReference=firebaseStorage.getReference().child( "LookSubtitle");
    private StorageReference ListorageReference=firebaseStorage.getReference().child( "ListenSubtitle");
    private File readFile;

    public ArrayList<SubtitleData> getListenSubtitleData(File filedirectory, int sectionNum, String idvideo, String key) {
        allOfSubtitle.clear();
        File file=filedirectory.getAbsoluteFile();
        FileDownloadTask fileDownloadTask;

        if(!file.exists())
            filedirectory.mkdirs();

        readFile= new File(file,key+".txt");
        Uri readFileUri=Uri.fromFile(readFile);
        fileDownloadTask=ListorageReference.child( idvideo ).child( String.valueOf( sectionNum )).child( key+".txt" ).getFile(readFileUri);
        parsingFile( key );
        return allOfSubtitle;
    }

    public ArrayList<SubtitleData> getLookSubtitleData(File filedirectory, int sectionNum, String idvideo, String key) {
        allOfSubtitle.clear();
        File file=filedirectory.getAbsoluteFile();
        FileDownloadTask fileDownloadTask;

        if(!file.exists())
            filedirectory.mkdirs();

        readFile= new File(file,key+".txt");
        Uri readFileUri=Uri.fromFile(readFile);
        fileDownloadTask=LookReference.child( idvideo ).child( String.valueOf( sectionNum )).child( key+".txt" ).getFile(readFileUri);
        parsingFile( key );

        return allOfSubtitle;
    }

    public void parsingFile(String key){
        String oneLine=null;
        try {
            Log.e( "inputsection","들어 왔어용" );
            FileReader fileReader=new FileReader( readFile );
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while((oneLine=bufferedReader.readLine())!=null){
                String[] arr=oneLine.split( "\t" );
                SubtitleData subtitleData=new SubtitleData( arr[0].trim(),arr[1].trim(),arr[2].trim() );
                allOfSubtitle.add( subtitleData );
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
