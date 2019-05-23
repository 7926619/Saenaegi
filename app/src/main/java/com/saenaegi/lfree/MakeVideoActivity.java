package com.saenaegi.lfree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.SubtitleController.InputDataController;

import java.io.File;
import java.util.ArrayList;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_DATETIME_VARIATION_TIME;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;
import com.saenaegi.lfree.SubtitleController.SubtitleData;

public class MakeVideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View view1;
    private ArrayList<SubtitleData> subtitles =new ArrayList<>();
    private Subtitle subtitle=new Subtitle();
    private InputDataController inputDataController;
    private File filedirectory;
    private YouTubePlayer player;
    private String videoID;
    private int sectionNum;
    private String idvideo;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_make_video);
        filedirectory=this.getCacheDir();
        getIdvideo();

        /* 액션바 상속 안하고 하기
        AppCompatCallback callback = new AppCompatCallback() {
            @Override
            public void onSupportActionModeStarted(ActionMode actionMode) {
            }

            @Override
            public void onSupportActionModeFinished(ActionMode actionMode) {
            }

            @Nullable
            @Override
            public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
                return null;
            }
        };

        AppCompatDelegate delegate = AppCompatDelegate.create(this, callback);*/
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
        //delegate.setSupportActionBar(toolbar);
        //delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        /* youtube_screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_height = size.x * 9 / 16;    // 16:9 비율

        View youtube_screen = (View) findViewById(R.id.youtube_screen);
        ViewGroup.LayoutParams params = youtube_screen.getLayoutParams();
        params.height = screen_height;
        youtube_screen.setLayoutParams(params);

        /* fab button */
        FloatingActionButton fab = findViewById(R.id.check_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDataController=new InputDataController();
                inputDataController.storeData(subtitle,subtitles,idvideo, filedirectory, sectionNum);
                Intent intent = new Intent(MakeVideoActivity.this, VideoCommentaryListActivity.class);  // 이동할 액티비티 수정해야됨
                startActivity(intent);
            }
        });

        /* add subtitle */
        ImageButton add_sub = (ImageButton) findViewById(R.id.add_subtitle);
        add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                createTableRow(view);
            }
        });

        /* 동영상 로드 및 초기화 */
        final Intent data = getIntent();
        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_screen);
        frag.initialize("AIzaSyAn_HFubCwx1rbM2q45hMGGhCPUx2AEOz4", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                videoID = data.getExtras().getString("link");
                player.loadVideo(videoID);

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                    }

                    @Override
                    public void onLoaded(String s) {
                    }

                    @Override
                    public void onAdStarted() {
                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    public void createTableRow(View v) {
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(lp);

        // section의 처음과 끝 시간-> 유투브 시간을 불러와서 넣어 주어야 한다. 미리 넣어 줘야 하는 것들
        subtitle.setSectionS("00:00");
        subtitle.setSectionF("10:00");
        subtitle.setIdgoogle("userid");
        subtitle.setName( "username" );
        sectionNum =2;
        subtitle.setRecommend(0);
        subtitle.setType( true );

        /* EditText */
        EditText startTime = new EditText(this);
        startTime.setHint("00:00");
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        startTime.setGravity(Gravity.CENTER);
        startTime.setInputType(TYPE_CLASS_DATETIME|TYPE_DATETIME_VARIATION_TIME);
        startTime.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
        EditText endTime = new EditText(this);
        endTime.setHint("00:00");
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        endTime.setGravity(Gravity.CENTER);
        endTime.setInputType(TYPE_CLASS_DATETIME|TYPE_DATETIME_VARIATION_TIME);
        endTime.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
        EditText subTitle = new EditText(this);
        subTitle.setHint("해설을 입력해주세요.");
        subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        setEditTextMaxLength(subTitle, 20);

        tr.addView(startTime);
        tr.addView(endTime);
        tr.addView(subTitle);

        /* subtitle ok & cancel button */
        LinearLayout ll = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = inflater.inflate(R.layout.layout_subtitle_menu, (ViewGroup) findViewById(R.id.subtitle_menu), false);

        ImageButton sub_ok = (ImageButton) childLayout.findViewById(R.id.sub_ok);
        sub_ok.setOnClickListener(new View.OnClickListener() {
            boolean checkInput(String start, String end, String sub) {
                if(start.length() == 0 || end.length() == 0 || sub.length() == 0) {
                    Toast.makeText(getApplicationContext(), "빈칸이 존재합니다.", Toast.LENGTH_LONG).show();
                    return true;
                }
                else if(start.length() != 5 || start.charAt(2) != ':') {
                    if(end.length() != 5 || end.charAt(2) != ':') {
                        Toast.makeText(getApplicationContext(), "시간의 형식이 알맞지 않습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "시작 시간의 형식이 알맞지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                else if(end.length() != 5 || end.charAt(2) != ':') {
                    Toast.makeText(getApplicationContext(), "종료 시간의 형식이 알맞지 않습니다.", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
            @Override
            public void onClick(View v) {
                // 등록 버튼
                ViewGroup parentView = (ViewGroup) v.getParent();
                ViewGroup parentView1 = (ViewGroup) parentView.getParent();     // (LinearLayout) ll
                ViewGroup parentView2 = (ViewGroup) parentView1.getParent();    // (TableLayout) tl
                int index = parentView2.indexOfChild(parentView1);  // (TableRow) tr's number
                ViewGroup row = (ViewGroup) parentView2.getChildAt(index-1);

                /* change to String */
                String start = ((EditText) row.getChildAt(0)).getText().toString();
                String end = ((EditText) row.getChildAt(1)).getText().toString();
                final String sub = ((EditText) row.getChildAt(2)).getText().toString();
                if(checkInput(start, end, sub)) {
                    return;
                }

                /* remove buttons */
                parentView2.removeView(parentView2.getChildAt(index));
                parentView2.removeView(parentView2.getChildAt(index-1));

                /* new TableRow with TextView */
                TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
                TableRow tr = new TableRow(MakeVideoActivity.this);
                tr.setPadding(0, 20, 0, 5);

                TextView t1 = new TextView(MakeVideoActivity.this);
                t1.setText(start);
                t1.setGravity(Gravity.CENTER);
                t1.setPadding(15, 0, 15, 0);

                TextView t2 = new TextView(MakeVideoActivity.this);
                t2.setText(end);
                t2.setGravity(Gravity.CENTER);
                t2.setPadding(15, 0, 15, 0);

                LinearLayout sub_col = new LinearLayout(MakeVideoActivity.this);
                sub_col.setOrientation(LinearLayout.HORIZONTAL);
                TextView t3 = new TextView(MakeVideoActivity.this);
                t3.setText(sub);

                /* subtitle needs 2 buttons */
                LinearLayout ll = new LinearLayout(MakeVideoActivity.this);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                ll.setLayoutParams(lparams);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                ImageView iv1 = new ImageView(MakeVideoActivity.this);
                iv1.setImageResource(R.drawable.play);
                ImageView iv2 = new ImageView(MakeVideoActivity.this);
                iv2.setImageResource(R.drawable.more_menu);
                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view1 = view;
                        PopupMenu p = new PopupMenu(getApplicationContext(), view);
                        getMenuInflater().inflate(R.menu.option_menu, p.getMenu());
                        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.m1:
                                        Toast.makeText(getApplication(), "수정", Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.m2:
                                        ViewGroup parentView = (ViewGroup) view1.getParent();
                                        ViewGroup parentView1 = (ViewGroup) parentView.getParent();
                                        ViewGroup parentView2 = (ViewGroup) parentView1.getParent();
                                        ViewGroup parentView3 = (ViewGroup) parentView2.getParent();
                                        int index = parentView3.indexOfChild(parentView2);  // (TableRow) tr's number
                                        int index2= subtitles.size()-index;
                                        subtitles.remove( index2 );
                                        parentView3.removeView(parentView3.getChildAt(index));
                                        parentView3.removeView(parentView3.getChildAt(index));  // line remove
                                        break;
                                }

                                return false;
                            }
                        });
                        p.show();
                    }
                });

                View blank = new View(MakeVideoActivity.this);
                blank.setBackgroundColor(Color.WHITE);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
                iv1.setLayoutParams(params);
                iv2.setLayoutParams(params);
                blank.setLayoutParams(params);
                ll.addView(iv1);
                ll.addView(blank);
                ll.addView(iv2);
                ll.setGravity(Gravity.END);

                sub_col.setPadding(15, 0, 15, 0);
                sub_col.addView(t3);
                sub_col.addView(ll);

                tr.addView(t1);
                tr.addView(t2);
                tr.addView(sub_col);

                SubtitleData subtitleData=new SubtitleData(start,end,sub);
                subtitles.add( subtitleData );

                /* line */
                View line = new View(MakeVideoActivity.this);
                line.setBackgroundColor(Color.parseColor("#d5d5d5"));

                /* add to TableLayout */
                tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tl.addView(line, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));

                /* show plus button */
                ViewGroup parentView3 = (ViewGroup)parentView2.getParent();
                ((ImageButton) parentView3.findViewById(R.id.add_subtitle)).setVisibility(View.VISIBLE);
            }
        });
        ImageButton sub_cancel = (ImageButton) childLayout.findViewById(R.id.sub_cancel);
        sub_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 취소 버튼
                ViewGroup parentView = (ViewGroup) v.getParent();
                ViewGroup parentView1 = (ViewGroup) parentView.getParent();     // (LinearLayout) ll
                ViewGroup parentView2 = (ViewGroup) parentView1.getParent();    // (TableLayout) tl
                int index = parentView2.indexOfChild(parentView1);  // (TableRow) tr's number
                parentView2.removeView(parentView2.getChildAt(index));
                parentView2.removeView(parentView2.getChildAt(index-1));

                /* show plus button */
                ViewGroup parentView3 = (ViewGroup)parentView2.getParent();
                ((ImageButton) parentView3.findViewById(R.id.add_subtitle)).setVisibility(View.VISIBLE);
            }
        });

        ll.addView(childLayout);
        ll.setGravity(Gravity.END);

        /* add to TableLayout */
        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tl.addView(ll, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void getIdvideo(){
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue( Video.class );
                    if(video.getLink().equals( videoID ))
                        idvideo=snapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    public void setEditTextMaxLength(EditText edt_text, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        edt_text.setFilters(filterArray);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                intent = new Intent(MakeVideoActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                break;
            case R.id.it_commentation:
                intent = new Intent(MakeVideoActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_request_video:
                intent = new Intent(MakeVideoActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_my_video:
                intent = new Intent(MakeVideoActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_like_video:
                intent = new Intent(MakeVideoActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.it_notice:
                intent = new Intent(MakeVideoActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.it_setting:
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
