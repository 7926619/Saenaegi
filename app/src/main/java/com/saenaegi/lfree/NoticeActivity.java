package com.saenaegi.lfree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Notice;
import com.saenaegi.lfree.RecycleviewController.Data;
import com.saenaegi.lfree.RecycleviewController.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoticeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private List<String> listTitle;
    private List<String> listContent;
    private ArrayList<Notice> notices=new ArrayList<>(  );
    private FirebaseDatabase firebase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebase.getReference().child( "LFREE" ).child( "NOTICE" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_notice);

        /* scroll on top */
        final ScrollView scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        scroll_view.post(new Runnable() {
            public void run() {
                scroll_view.scrollTo(0, 0);
            }
        });

        /* Action Bar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* navigation */
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        final ImageButton drawerButton = (ImageButton)findViewById(R.id.drawer_icon);
        drawerButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        /* recycle view */
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {

        // 임의의 데이터입니다.
        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notices.clear();
                adapter = new RecyclerAdapter();
                recyclerView.setAdapter(adapter);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notice notice=snapshot.getValue(Notice.class);
                    notices.add(notice);
                    Data data = new Data();
                    data.setTitle(notice.getSubtitle());
                    data.setSub(notice.getTime());
                    data.setContent(notice.getText());
                    adapter.addItem( data );
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                intent = new Intent(NoticeActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                break;
            case R.id.it_commentation:
                intent = new Intent(NoticeActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_request_video:
                intent = new Intent(NoticeActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_my_video:
                intent = new Intent(NoticeActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_like_video:
                intent = new Intent(NoticeActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_notice:
                break;
            case R.id.it_setting:
                intent = new Intent(NoticeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}