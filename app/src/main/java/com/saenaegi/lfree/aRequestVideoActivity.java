package com.saenaegi.lfree;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.saenaegi.lfree.Data.Video;

public class aRequestVideoActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private ArrayList<Video> videos=new ArrayList<>(  );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_request_video);
    }
}
