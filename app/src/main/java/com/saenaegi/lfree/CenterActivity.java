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

public class CenterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* navigation */
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        final ImageButton drawerButton = (ImageButton) findViewById(R.id.drawer_icon);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        /* Text View */
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(R.string.centerText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                intent = new Intent(CenterActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_commentation:
                intent = new Intent(CenterActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                intent = new Intent(CenterActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_my_video:
                intent = new Intent(CenterActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                intent = new Intent(CenterActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_notice:
                intent = new Intent(CenterActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_setting:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
