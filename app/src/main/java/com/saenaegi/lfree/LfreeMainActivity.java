package com.saenaegi.lfree;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.saenaegi.lfree.RecycleviewController.RecyclerAdapter;
import com.saenaegi.lfree.RecycleviewController.Data;

import java.util.Arrays;
import java.util.List;

public class LfreeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerAdapter adapter1, adapter2;

    public FirebaseAuth firebaseAuth;
    TextView LoginUserProfile;

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
        RecyclerView recyclerView1 = findViewById(R.id.complete_list);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        adapter1 = new RecyclerAdapter();
        recyclerView1.setAdapter(adapter1);

        RecyclerView recyclerView2 = findViewById(R.id.making_list);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapter2 = new RecyclerAdapter();
        recyclerView2.setAdapter(adapter2);

        getData();
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화가나다라마바사아자차카", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setResId(R.drawable.icon);

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter1.addItem(data);
            adapter2.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
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
                break;
            case R.id.it_request_video:
                intent = new Intent(LfreeMainActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_my_video:
                intent = new Intent(LfreeMainActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_like_video:
                intent = new Intent(LfreeMainActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_notice:
                intent = new Intent(LfreeMainActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.it_setting:
                intent = new Intent(LfreeMainActivity.this, SettingsActivity.class);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
