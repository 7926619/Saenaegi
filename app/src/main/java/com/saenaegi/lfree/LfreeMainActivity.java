package com.saenaegi.lfree;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import Data.DataManage;

public class LfreeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DataManage dbtest=new DataManage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lfree_main);

        /* Action Bar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);

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
        boolean check= dbtest.setUserQuery( "example1","example1", true);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.it_home:
                Toast.makeText(this, "it_home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_commentation:
                Toast.makeText(this, "it_commentation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_request_video:
                Toast.makeText(this, "it_request_video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_my_video:
                Toast.makeText(this, "it_my_video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_like_video:
                Toast.makeText(this, "it_like_video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_notice:
                Toast.makeText(this, "it_notice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_setting:
                Toast.makeText(this, "it_setting", Toast.LENGTH_SHORT).show();
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
