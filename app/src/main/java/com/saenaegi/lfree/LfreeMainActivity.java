package com.saenaegi.lfree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.RecycleviewController.RecyclerAdapter;
import com.saenaegi.lfree.RecycleviewController.Data;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LfreeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnListItemSelectedInterface {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerAdapter adapter1, adapter2;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    private ArrayList<Video> pvideos=new ArrayList<>(  );
    private ArrayList<Video> mvideos=new ArrayList<>(  );
    public FirebaseAuth firebaseAuth;
    TextView LoginUserProfile;
    private Bitmap thumb;
    private String url;

    private ProgressBar progressBar1, progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_lfree_main);

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
        toolbar.setTitle("");
        toolbar.setSubtitle("");

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

        /* buttons */
        ImageButton comButton = (ImageButton) findViewById(R.id.commentButton);
        comButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(LfreeMainActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton reqButton = (ImageButton) findViewById(R.id.requestButton);
        reqButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(LfreeMainActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
            }
        });

        /*
        View headerView = navigationView.getHeaderView(0);
        LoginUserProfile = (TextView)headerView.findViewById(R.id.textView10);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        LoginUserProfile.setText(user.getDisplayName() + "님\n" + user.getEmail());
        */

        /* recycle view */
        recyclerView1 = findViewById(R.id.complete_list);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        recyclerView2 = findViewById(R.id.making_list);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapter1 = new RecyclerAdapter(this, this);
        adapter2 = new RecyclerAdapter(this, this);
        recyclerView1.setAdapter( adapter1 );
        recyclerView2.setAdapter( adapter2 );

        getData();

        /* progress bar */
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar1.setMax(100);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setMax(100);

        /* footer */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.footer, new FooterFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onItemSelected(View v, int position) {
        View view = (View) v.getParent();
        View parentView = (View) view.getParent();
        if(parentView.equals(recyclerView1)) {
            Intent intent = new Intent(LfreeMainActivity.this, WatchVideoActivity.class);
            intent.putExtra("link", pvideos.get(position).getLink());
            intent.putExtra("count", pvideos.get(position).getSectionCount());
            startActivity(intent);
        } else if(parentView.equals(recyclerView2)) {
            Intent intent = new Intent(LfreeMainActivity.this, WatchVideoActivity.class);
            intent.putExtra("link", mvideos.get(position).getLink());
            intent.putExtra("count", mvideos.get(position).getSectionCount());
            startActivity(intent);
        }
    }

    private void getData() {

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter1.delItem();
                adapter2.delItem();
                pvideos.clear();
                mvideos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);

                    if(video.isListenstate()&&video.isLookstate()) {
                        pvideos.add(video);
                        Data data=new Data();
                        data.setTitle( video.getTitle() );
                        data.setBit( StringToBitMap(video.getBitt()) );
                        adapter1.addItem( data );
                    }
                    else{
                        mvideos.add(video);
                        Data data=new Data();
                        data.setTitle( video.getTitle() );
                        data.setBit( StringToBitMap(video.getBitt()) );
                        adapter2.addItem( data );
                    }
                }
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                progressBar1.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        // 임의의 데이터입니다.
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                break;
            case R.id.it_commentation:
                intent = new Intent(LfreeMainActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                intent = new Intent(LfreeMainActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_my_video:
                intent = new Intent(LfreeMainActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                intent = new Intent(LfreeMainActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_notice:
                intent = new Intent(LfreeMainActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_setting:
                intent = new Intent(LfreeMainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    // Sign In function Starts From Here.
    public void UserSignOutMethod(){
        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(LfreeMainActivity.this, "로그아웃!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LfreeMainActivity.this, MainActivity.class));
            finish();
        }
    }

    private long time= 0;
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"\'뒤로\' 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            } else if (System.currentTimeMillis() - time < 2000) {
                ActivityCompat.finishAffinity(this);
            }
        }
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
