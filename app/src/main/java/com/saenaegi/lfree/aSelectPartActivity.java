package com.saenaegi.lfree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class aSelectPartActivity extends AppCompatActivity {

    private ListView listView;
    private aListviewAdapter adapter;
    private ArrayList<aListviewItem> sdata = new ArrayList<>();
    private String videoID;
    private String[] madesection;
    private int sectionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_select_part);

        final Intent data=getIntent();
        videoID = data.getExtras().getString("link");
        sectionCount=data.getExtras().getInt( "count" );
        madesection=data.getExtras().getStringArray( "madesection" );

        sectionCount=10;
        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, sdata);
        listView.setAdapter(adapter);

        //만들어 진 secton Num만 출력이 되는 지 확인 -> subtitle의 type의 값이 false인 데이터만 취급해야 한다.
        sdata.clear();
        for(int i=0;i<madesection.length;i++){
            if(madesection[i]!=null) {
                aListviewItem temp = new aListviewItem( madesection[i] );
                sdata.add( temp );
            }
        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(aSelectPartActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videoID);
                intent.putExtra("count",sectionCount);
                intent.putExtra( "nowSection", madesection[position]);
                startActivity(intent);
            }
        } );

        adapter.notifyDataSetChanged();
    }
}
