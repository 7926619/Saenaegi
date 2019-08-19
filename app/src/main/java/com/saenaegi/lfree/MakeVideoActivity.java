package com.saenaegi.lfree;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.SubtitleController.InputDataController;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.saenaegi.lfree.SubtitleController.SubtitleData;

public class MakeVideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View view1;
    private ArrayList<SubtitleData> subtitles =new ArrayList<>();
    private Subtitle subtitle=new Subtitle();
    private ArrayList<String> madeSection=new ArrayList<>();
    private boolean modify=false;
    private String idsubtitle;
    private InputDataController inputDataController;
    private File filedirectory;
    private YouTubePlayer player;
    private String videoID;
    private int sectionCount;
    private int sectionNum;
    private int allSectionCount;
    private String idvideo;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private int min;
    private int sec;
    private TextView subtitlebox;
    private boolean ty;
    private TextToSpeech tts;
    private FirebaseAuth firebaseAuth;
    private TextView LoginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_make_video);
        filedirectory=this.getCacheDir();

        Log.e("name",""+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Log.e("name",""+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
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

        /* 구글 정보 불러오기 */
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser googleUser = firebaseAuth.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        LoginUserName = (TextView)headerView.findViewById(R.id.textView10);
        LoginUserName.setText(googleUser.getDisplayName() + "님");

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
                inputDataController = new InputDataController();
                if(modify == false) {
                    inputDataController.storeData( subtitle, subtitles, idvideo, filedirectory, sectionNum );
                    String tmp= String.valueOf(sectionNum);
                    boolean check=true;
                    for(String string:madeSection){
                        if(string.equals(tmp)){
                            check=false;
                        }
                    }
                    if(check) {
                        madeSection.add( tmp );
                        if(madeSection.size()==allSectionCount){
                            if(ty)
                             databaseReference.child( idvideo ).child("listenstate").setValue( true );
                            else
                             databaseReference.child( idvideo ).child("lookstate").setValue( true );
                        }
                    }
                }
                else{
                    inputDataController.modifyData( idsubtitle, subtitles, idvideo, filedirectory, sectionNum ,ty);
                }
                tts.shutdown();
                Intent intent = new Intent(MakeVideoActivity.this, WatchVideoActivity.class);
                intent.putExtra("link", videoID);
                intent.putExtra("count", sectionCount);
                setResult(3, intent);
                startActivity(intent);
                finish();
            }
        });

        /* add subtitle */
        ImageButton add_sub = (ImageButton) findViewById(R.id.add_subtitle);
        add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                createTableRow(null);
            }
        });

        subtitlebox = (TextView) findViewById(R.id.subtitle);

        /* 전자친구 */
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREAN);
                }
                else
                    Toast.makeText(getApplication(), "TTS : TTS's Initialization is Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        /* 동영상 로드 및 초기화 */
        final Intent data = getIntent();
        videoID = data.getExtras().getString("link");
        sectionCount = data.getExtras().getInt("count");
        modify = data.getExtras().getBoolean( "modify" );
        ty = data.getExtras().getBoolean("type");
        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_screen);
        frag.initialize("AIzaSyAn_HFubCwx1rbM2q45hMGGhCPUx2AEOz4", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.loadVideo(videoID);

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                    }

                    @Override
                    public void onLoaded(String s) {
                        min = player.getDurationMillis()/60000;
                        sec = (player.getDurationMillis()%60000)/1000;
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

        getIdvideo();

        // true는 수정 값이 있다는 것이다.
        if(modify == true) {
            subtitles = data.getParcelableArrayListExtra( "subtitles" );
            idsubtitle = data.getExtras().getString( "idsubtitle" );
            modifyDataView();
        }
        /* footer */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.footer, new FooterFragment());
        fragmentTransaction.commit();
    }

    public void modifyDataView() {
        Intent data = getIntent();
        // section의 처음과 끝 시간-> 유투브 시간을 불러와서 넣어 주어야 한다. 미리 넣어 줘야 하는 것들
        sectionNum = Integer.parseInt(data.getExtras().getString("part"));
        if(min < 10) {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF("0"+min+":0"+sec);
            else
                subtitle.setSectionF("0"+min+":"+sec);
        }
        else if((sectionNum > (min / 10)) && ((min % 10) > 3)){
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF(min+":0"+sec);
            else
                subtitle.setSectionF(min+":"+sec);
        }
        else if(sectionNum == (min / 10) && ((min % 10) < 4)) {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF(min+":0"+sec);
            else
                subtitle.setSectionF(min+":"+sec);
        }
        else {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            subtitle.setSectionF(sectionNum+"0:00");
        }
        subtitle.setIdgoogle( FirebaseAuth.getInstance().getCurrentUser().getEmail() );
        subtitle.setName( FirebaseAuth.getInstance().getCurrentUser().getDisplayName() );
        subtitle.setRecommend(0);
        subtitle.setType( true );
        if(data.getExtras().getBoolean("type"))
            subtitle.setType( true );
        else
            subtitle.setType( false );

        /* new TableRow with TextView */
        for(SubtitleData s : subtitles) {
            creatTextViewRow(s, false);
        }
    }

    void creatTextViewRow(SubtitleData s, Boolean check) {  // check는 수정 중 취소를 눌렀을 때 true
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(lp);
        tr.setPadding(0, 20, 0, 5);

        final String start = s.getSectionS();
        final String end = s.getSectionE();
        final String sub = s.getSubString();

        TextView t1 = new TextView(this);
        t1.setText(start);
        t1.setGravity(Gravity.CENTER);
        t1.setPadding(15, 0, 15, 0);

        TextView t2 = new TextView(this);
        t2.setText(end);
        t2.setGravity(Gravity.CENTER);
        t2.setPadding(15, 0, 15, 0);

        LinearLayout sub_col = new LinearLayout(this);
        sub_col.setOrientation(LinearLayout.HORIZONTAL);
        TextView t3 = new TextView(this);
        t3.setText(sub);
        t3.setMaxWidth(400);

        /* subtitle needs 2 buttons */
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        ll.setLayoutParams(lparams);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.play);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ty){
                    if(!player.isPlaying())
                        player.play();
                    subtitlebox.setText(sub);
                    int spoint = ((Integer.parseInt(start.split(":")[0])*60) + Integer.parseInt(start.split(":")[1])) * 1000;
                    player.seekToMillis(spoint);
                    final int epoint = ((Integer.parseInt(end.split(":")[0])*60) + Integer.parseInt(end.split(":")[1]));
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                if((player.getCurrentTimeMillis()/1000) >= epoint){
                                    subtitlebox.setText("");
                                    player.pause();
                                    break;
                                }
                            }
                        }
                    });
                    th.start();
                }
                else {
                    if(!player.isPlaying())
                        player.play();
                    int spoint = ((Integer.parseInt(start.split(":")[0])*60) + Integer.parseInt(start.split(":")[1])) * 1000;
                    player.seekToMillis(spoint);
                    tts.setSpeechRate((float)0.87);
                    tts.speak(sub, TextToSpeech.QUEUE_FLUSH, null);
                    final int epoint = ((Integer.parseInt(end.split(":")[0])*60) + Integer.parseInt(end.split(":")[1]));
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                if((player.getCurrentTimeMillis()/1000) >= epoint){
                                    tts.stop();
                                    player.pause();
                                    break;
                                }
                            }
                        }
                    });
                    th.start();
                }
            }
        });

        ImageView iv2 = new ImageView(this);
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
                        ViewGroup parentView, parentView1, parentView2, parentView3, parentView4;
                        int index;
                        switch (item.getItemId()) {
                            case R.id.m1:
                                parentView = (ViewGroup) view1.getParent();
                                parentView1 = (ViewGroup) parentView.getParent();
                                parentView2 = (ViewGroup) parentView1.getParent();
                                parentView3 = (ViewGroup) parentView2.getParent();
                                index = parentView3.indexOfChild(parentView2);  // (TableRow) tr's
                                parentView3.removeView(parentView3.getChildAt(index));
                                parentView3.removeView(parentView3.getChildAt(index));  // line remove
                                parentView4 = (ViewGroup) parentView3.getParent();
                                ((ImageButton) parentView4.findViewById(R.id.add_subtitle)).setVisibility(View.INVISIBLE);
                                createTableRow(subtitles.get((index - 1) / 2));
                                break;
                            case R.id.m2:
                                parentView = (ViewGroup) view1.getParent();
                                parentView1 = (ViewGroup) parentView.getParent();
                                parentView2 = (ViewGroup) parentView1.getParent();
                                parentView3 = (ViewGroup) parentView2.getParent();
                                index = parentView3.indexOfChild(parentView2);  // (TableRow) tr's number
                                subtitles.remove((index - 1) / 2);
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

        View blank = new View(this);
        blank.setBackgroundColor(Color.WHITE);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        /* line */
        View line = new View(MakeVideoActivity.this);
        line.setBackgroundColor(Color.parseColor("#d5d5d5"));

        /* add to TableLayout */
        if(check) {
            tl.addView(tr, subtitles.indexOf(s)*2+1, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tl.addView(line, subtitles.indexOf(s)*2+2, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));
        } else {
            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tl.addView(line, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));
        }
    }

    public void createTableRow(final SubtitleData subData) {
        final int subIndex = subtitles.indexOf(subData);
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(lp);

        Intent data = getIntent();
        // section의 처음과 끝 시간-> 유투브 시간을 불러와서 넣어 주어야 한다. 미리 넣어 줘야 하는 것들
        if(modify == true) {
            sectionNum = Integer.parseInt(data.getExtras().getString("part"));
        } else {
            sectionNum = Integer.parseInt(data.getExtras().getString("part"));
        }
        if(min < 10) {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF("0"+min+":0"+sec);
            else
                subtitle.setSectionF("0"+min+":"+sec);
        }
        else if((sectionNum > (min / 10)) && ((min % 10) > 3)){
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF(min+":0"+sec);
            else
                subtitle.setSectionF(min+":"+sec);
        }
        else if(sectionNum == (min / 10) && ((min % 10) < 4)) {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            if(sec < 10)
                subtitle.setSectionF(min+":0"+sec);
            else
                subtitle.setSectionF(min+":"+sec);
        }
        else {
            subtitle.setSectionS((sectionNum-1)+"0:00");
            subtitle.setSectionF(sectionNum+"0:00");
        }
        subtitle.setIdgoogle( FirebaseAuth.getInstance().getCurrentUser().getEmail() );
        subtitle.setName( FirebaseAuth.getInstance().getCurrentUser().getDisplayName() );
        subtitle.setRecommend(0);
        subtitle.setType( true );
        if(ty)
            subtitle.setType( true );
        else
            subtitle.setType( false );

        /* EditText */
        EditText startTime = new EditText(this);
        startTime.setHint("00:00");
        startTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        startTime.setGravity(Gravity.CENTER);
        startTime.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
        final EditText endTime = new EditText(this);
        endTime.setHint("00:00");
        endTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        endTime.setGravity(Gravity.CENTER);
        endTime.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
        final EditText subTitle = new EditText(this);
        subTitle.setHint("해설을 입력해주세요.");
        subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        subTitle.setHorizontallyScrolling(true);
        setEditTextMaxLength(subTitle, 20);

        if(subData != null) {
            startTime.setText(subData.getSectionS());
            endTime.setText(subData.getSectionE());
            subTitle.setText(subData.getSubString());
        }

        tr.addView(startTime);
        tr.addView(endTime);
        tr.addView(subTitle);

        /* subtitle ok & cancel button */
        LinearLayout ll = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = inflater.inflate(R.layout.layout_subtitle_menu, (ViewGroup) findViewById(R.id.subtitle_menu), false);

        ImageButton sub_ok = (ImageButton) childLayout.findViewById(R.id.sub_ok);
        sub_ok.setOnClickListener(new View.OnClickListener() {
            int subTime(String buf) {
                if(buf.charAt(0) == '0')
                    return Integer.parseInt(buf.substring(1, 2));
                return Integer.parseInt(buf.substring(0, 2));
            }
            boolean checkInput(String start, String end, String sub) {
                int startTime, endTime, preEndTime, nextStartTime, sectionST, sectionFT;
                SubtitleData preSub, nextSub;

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

                startTime = subTime(start.substring(0, 2)) * 60 + subTime(start.substring(3, 5));
                endTime = subTime(end.substring(0, 2)) * 60 + subTime(end.substring(3, 5));
                sectionST = subTime(subtitle.getSectionS().substring(0,2)) * 60 + subTime(subtitle.getSectionS().substring(3,5));
                sectionFT = subTime(subtitle.getSectionF().substring(0,2)) * 60 + subTime(subtitle.getSectionF().substring(3,5));

                if (startTime > endTime) {
                    Toast.makeText(getApplicationContext(), "종료 시간이 시작 시간보다 빠릅니다.", Toast.LENGTH_LONG).show();
                    return true;
                } else if (sectionST > startTime || startTime > sectionFT || endTime < sectionST || endTime > sectionFT) {
                    Toast.makeText(getApplicationContext(), "해당 파트의 시간은 "+subtitle.getSectionS()+" ~ "+subtitle.getSectionF()+" 입니다.", Toast.LENGTH_LONG).show();
                    return true;
                } else if (subtitles.size() != 0 && subIndex != 0) {
                    if(subData != null) {
                        preSub = subtitles.get(subIndex - 1);
                    } else {
                        preSub = subtitles.get(subtitles.size() - 1);
                    }
                    preEndTime = subTime(preSub.getSectionE().substring(0, 2)) * 60 + subTime(preSub.getSectionE().substring(3, 5));
                    if (preEndTime >= startTime) {
                        Toast.makeText(getApplicationContext(), "시작 시간이 이전 종료 시간보다 같거나 빠릅니다.", Toast.LENGTH_LONG).show();
                        return true;
                    }
                } else if (subtitles.size() != 0 && subIndex != (subtitles.size() - 1)) {
                    nextSub = subtitles.get(subIndex + 1);
                    nextStartTime = subTime(nextSub.getSectionS().substring(0, 2)) * 60 + subTime(nextSub.getSectionS().substring(3, 5));
                    if (endTime >= nextStartTime) {
                        Toast.makeText(getApplicationContext(), "종료 시간이 다음 시작 시간보다 같거나 빠릅니다.", Toast.LENGTH_LONG).show();
                        return true;
                    }
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
                final String start = ((EditText) row.getChildAt(0)).getText().toString();
                final String end = ((EditText) row.getChildAt(1)).getText().toString();
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
                t3.setMaxWidth(400);

                /* subtitle needs 2 buttons */
                LinearLayout ll = new LinearLayout(MakeVideoActivity.this);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                ll.setLayoutParams(lparams);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                ImageView iv1 = new ImageView(MakeVideoActivity.this);
                iv1.setImageResource(R.drawable.play);
                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ty){
                            if(!player.isPlaying())
                                player.play();
                            subtitlebox.setText(sub);
                            int spoint = ((Integer.parseInt(start.split(":")[0])*60) + Integer.parseInt(start.split(":")[1])) * 1000;
                            player.seekToMillis(spoint);
                            final int epoint = ((Integer.parseInt(end.split(":")[0])*60) + Integer.parseInt(end.split(":")[1]));
                            Thread th = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(true) {
                                        if((player.getCurrentTimeMillis()/1000) >= epoint){
                                            subtitlebox.setText("");
                                            player.pause();
                                            break;
                                        }
                                    }
                                }
                            });
                            th.start();
                        }
                        else {
                            if(!player.isPlaying())
                                player.play();
                            int spoint = ((Integer.parseInt(start.split(":")[0])*60) + Integer.parseInt(start.split(":")[1])) * 1000;
                            player.seekToMillis(spoint);
                            tts.setSpeechRate((float)0.87);
                            tts.speak(sub, TextToSpeech.QUEUE_FLUSH, null);
                            final int epoint = ((Integer.parseInt(end.split(":")[0])*60) + Integer.parseInt(end.split(":")[1]));
                            Thread th = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(true) {
                                        if((player.getCurrentTimeMillis()/1000) >= epoint){
                                            tts.stop();
                                            player.pause();
                                            break;
                                        }
                                    }
                                }
                            });
                            th.start();
                        }
                    }
                });
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
                                ViewGroup parentView, parentView1, parentView2, parentView3, parentView4;
                                int index;
                                switch (item.getItemId()) {
                                    case R.id.m1:
                                        parentView = (ViewGroup) view1.getParent();
                                        parentView1 = (ViewGroup) parentView.getParent();
                                        parentView2 = (ViewGroup) parentView1.getParent();
                                        parentView3 = (ViewGroup) parentView2.getParent();
                                        index = parentView3.indexOfChild(parentView2);  // (TableRow) tr's
                                        parentView3.removeView(parentView3.getChildAt(index));
                                        parentView3.removeView(parentView3.getChildAt(index));  // line remove
                                        parentView4 = (ViewGroup) parentView3.getParent();
                                        ((ImageButton) parentView4.findViewById(R.id.add_subtitle)).setVisibility(View.INVISIBLE);
                                        createTableRow(subtitles.get((index - 1) / 2));
                                        break;
                                    case R.id.m2:
                                        parentView = (ViewGroup) view1.getParent();
                                        parentView1 = (ViewGroup) parentView.getParent();
                                        parentView2 = (ViewGroup) parentView1.getParent();
                                        parentView3 = (ViewGroup) parentView2.getParent();
                                        index = parentView3.indexOfChild(parentView2);  // (TableRow) tr's number
                                        subtitles.remove((index - 1) / 2);
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

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, ViewGroup.LayoutParams.WRAP_CONTENT);
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

                SubtitleData subtitleData = new SubtitleData(start,end,sub);
                if(subData != null) {
                    subtitles.set(subIndex, subtitleData);
                }
                else {
                    subtitles.add(subtitleData);
                }

                /* line */
                View line = new View(MakeVideoActivity.this);
                line.setBackgroundColor(Color.parseColor("#d5d5d5"));

                /* add to TableLayout */
                if(subData != null) {
                    tl.addView(tr, subIndex*2+1, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tl.addView(line, subIndex*2+2, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));
                } else {
                    tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tl.addView(line, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 1));
                }

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
                parentView2.removeView(parentView2.getChildAt(index - 1));
                if(subData != null) {
                    creatTextViewRow(subData, true);
                }

                /* show plus button */
                ViewGroup parentView3 = (ViewGroup)parentView2.getParent();
                ((ImageButton) parentView3.findViewById(R.id.add_subtitle)).setVisibility(View.VISIBLE);
            }
        });

        ll.addView(childLayout);
        ll.setGravity(Gravity.END);

        /* add to TableLayout */
        if(subData != null) {
            tl.addView(tr, subIndex*2+1, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tl.addView(ll, subIndex*2+2, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        else {
            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tl.addView(ll, new TableLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void getIdvideo(){
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue( Video.class );
                    if(video.getLink().equals( videoID )) {
                        idvideo = snapshot.getKey();
                        allSectionCount=video.getSectionCount();
                        for(DataSnapshot snapshot1:snapshot.child( "SUBTITLE" ).getChildren()) {
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                Subtitle temp = snapshot2.getValue( Subtitle.class );
                                if (temp.isType() == ty) {
                                    madeSection.add( snapshot1.getKey() );
                                    break;
                                }
                            }
                        }
                        break;
                    }
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
                intent = new Intent(MakeVideoActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_commentation:
                intent = new Intent(MakeVideoActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                intent = new Intent(MakeVideoActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_my_video:
                intent = new Intent(MakeVideoActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                intent = new Intent(MakeVideoActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_notice:
                intent = new Intent(MakeVideoActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_setting:
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
}
