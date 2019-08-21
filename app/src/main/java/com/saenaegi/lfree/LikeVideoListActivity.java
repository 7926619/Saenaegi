package com.saenaegi.lfree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.ListviewAdapter;
import com.saenaegi.lfree.ListviewController.ListviewItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LikeVideoListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar progressBar;
    private ScrollView scroll_view;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" );
    private DatabaseReference dataRef=firebaseDatabase.getReference().child( "LFREE" ).child( "TEMP" );
    private ArrayList<Video> videos=new ArrayList<>();
    private ArrayList<String> lvideos=new ArrayList<>();
    private ListView listView;
    private ListviewAdapter adapter;
    private ArrayList<ListviewItem> data=new ArrayList<>(  );
    private FirebaseAuth firebaseAuth;
    private TextView LoginUserName;
    private TextView Recommend;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_like_video_list);

        /* scroll on top */
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
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

        /* 구글 정보 불러오기 */
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleUser = firebaseAuth.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        LoginUserName = (TextView)headerView.findViewById(R.id.textView10);
        LoginUserName.setText(googleUser.getDisplayName() + "님");
        Recommend = (TextView)headerView.findViewById(R.id.textView11);
        Recommend.setText("로딩중...");

        CircularImageView user_profile = headerView.findViewById(R.id.imageView);
        Thread mThread= new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(googleUser.getPhotoUrl().toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException ee) {
                    android.util.Log.d(null, "==================> " + ee.toString());
                }catch (IOException e){
                    android.util.Log.d(null, "==================> " + e.toString());
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            user_profile.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            android.util.Log.d(null, "==================> " + e.toString());
        }

        /* list view */
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewAdapter(this, R.layout.listview_item, data);
        listView.setAdapter(adapter);

        changeView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LikeVideoListActivity.this, WatchVideoActivity.class);
                intent.putExtra("link",videos.get(position).getLink());
                intent.putExtra("count",videos.get(position).getSectionCount());
                startActivity(intent);
            }
        });

        /* progress bar */
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        /* footer */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.footer, new FooterFragment());
        fragmentTransaction.commit();
    }

    private void changeView() {
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvideos.clear();
                videos.clear();
                data.clear();

                for(DataSnapshot snapshot:dataSnapshot.child( "LIKEVIDEO" ).getChildren()){
                    if(snapshot.getKey().equals( FirebaseAuth.getInstance().getCurrentUser().getDisplayName() )){
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            lvideos.add( snapshot1.getKey() );
                        }
                    }
                }
                for(DataSnapshot snapshot:dataSnapshot.child( "VIDEO" ).getChildren()){
                    for(String idvideo:lvideos){
                        if(snapshot.getKey().equals( idvideo )){
                            Video video=snapshot.getValue(Video.class);
                            videos.add( video );
                            ListviewItem temp = new ListviewItem( StringToBitMap(video.getBitt()), video.getTitle(), "조회수 : "+video.getView() );
                            data.add( temp );
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren( listView );
                progressBar.setVisibility(View.GONE);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        dataRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser googleIUser=firebaseAuth.getCurrentUser();
                String recommend=null;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals( googleIUser.getDisplayName() )){
                        recommend=snapshot.getValue(String.class);
                    }
                }

                View headerView = navigationView.getHeaderView(0);
                Recommend.setText(recommend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.search_icon);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setQueryHint("Search");

        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.search);
        v.setPadding(0,0,0,0);

        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                changeView();
                scroll_view.scrollTo(0,0);
                return true;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                data.clear();
                for(Video video:videos){
                    if(video.getTitle().contains( query )){
                        ListviewItem listviewItem = new ListviewItem(StringToBitMap(video.getBitt()),video.getTitle(), "조회수 : "+video.getView());
                        data.add(listviewItem);
                    }
                }
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren( listView );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                intent = new Intent(LikeVideoListActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_commentation:
                intent = new Intent(LikeVideoListActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                intent = new Intent(LikeVideoListActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_my_video:
                intent = new Intent(LikeVideoListActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                break;
            case R.id.it_notice:
                intent = new Intent(LikeVideoListActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_setting:
                intent = new Intent(LikeVideoListActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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
        }catch(OutOfMemoryError e){
            android.util.Log.d(null, "==================> " + e.toString());
            return null;
        }
    }
}