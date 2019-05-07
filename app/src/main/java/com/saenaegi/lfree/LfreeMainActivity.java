package com.saenaegi.lfree;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LfreeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
