package com.saenaegi.lfree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.ListviewController.ListviewAdapter;
import com.saenaegi.lfree.ListviewController.ListviewItem;

import java.util.ArrayList;

import com.saenaegi.lfree.Data.Video;

public class VideoCommentaryListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    private ListviewAdapter adapter;
    private ListView listView;
    private ArrayList<Video> videos=new ArrayList<>();
    private ArrayList<ListviewItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_video_commentary_list);

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

        /* Tab */
        changeView(0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout1);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택 상태로 변경.
                int pos = tab.getPosition() ;
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });
    }

    private void changeView(int index) {
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewAdapter(this, R.layout.listview_item, data);
        listView.setAdapter(adapter);

        switch (index) {
            case 0 :
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        data.clear();
                        videos.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Video video=snapshot.getValue(Video.class);
                            videos.add( video );
                            if (video.isLookstate() && video.isListenstate()) {
                                ListviewItem temp = new ListviewItem( R.drawable.icon, video.getLink(), String.valueOf( video.getView() ) );
                                data.add( temp );
                            }
                        }
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listView);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
                break;

            case 1 :
                data.clear();
                StringBuilder stringBuilder;
                for(Video video:videos){
                    if(!video.isListenstate()&&!video.isLookstate()){
                        stringBuilder=new StringBuilder();
                        stringBuilder.append( "조회수 : " );
                        stringBuilder.append( video.getView() );
                        stringBuilder.append(" / ");
                        stringBuilder.append( video.noTrueAllState() );
                        ListviewItem temp = new ListviewItem(R.drawable.icon,video.getLink(),stringBuilder.toString());
                        data.add( temp );
                    }
                    else if(video.isListenstate()==false){
                        stringBuilder=new StringBuilder();
                        stringBuilder.append( "조회수 : " );
                        stringBuilder.append( video.getView() );
                        stringBuilder.append(" / ");
                        stringBuilder.append( video.noTrueListenstate() );
                        ListviewItem temp = new ListviewItem(R.drawable.icon,video.getLink(),stringBuilder.toString());
                        data.add( temp );
                    }
                    else if(video.isLookstate()==false){
                        stringBuilder=new StringBuilder();
                        stringBuilder.append( "조회수 : " );
                        stringBuilder.append( video.getView() );
                        stringBuilder.append(" / ");
                        stringBuilder.append( video.noTrueLookstate() );
                        ListviewItem temp = new ListviewItem(R.drawable.icon,video.getLink(),stringBuilder.toString());
                        data.add( temp );
                    }
                }
                setListViewHeightBasedOnChildren(listView);
                break;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoCommentaryListActivity.this, WatchVideoActivity.class);
                startActivity(intent);
            }
        });
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

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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
                intent = new Intent(VideoCommentaryListActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                break;
            case R.id.it_commentation:
                break;
            case R.id.it_request_video:
                intent = new Intent(VideoCommentaryListActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_my_video:
                intent = new Intent(VideoCommentaryListActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_like_video:
                intent = new Intent(VideoCommentaryListActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_notice:
                intent = new Intent(VideoCommentaryListActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.it_setting:
                intent = new Intent(VideoCommentaryListActivity.this, SettingsActivity.class);
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
