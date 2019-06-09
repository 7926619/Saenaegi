package com.saenaegi.lfree;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.RecycleviewController_p.Data;
import com.saenaegi.lfree.RecycleviewController_p.RecyclerAdapter;
import com.saenaegi.lfree.RecycleviewController_s.DataS;
import com.saenaegi.lfree.RecycleviewController_s.RecyclerAdapterS;
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;
import com.saenaegi.lfree.SubtitleController.outputDataController;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnListItemSelectedInterface, RecyclerAdapterS.OnListItemSelectedInterface {

    private static int posi;
    private int prePosition = -1;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerAdapter adapter1;
    private RecyclerAdapterS adapter2;
    private RecyclerView recyclerView1, recyclerView2;
    private YouTubePlayer player;
    private String videoID;
    private String idvideo;
    private int sectionCount;
    private File filedirectory;
    private outputDataController output;
    private ArrayList<Boolean> listState=new ArrayList<>();
    private HashMap<String, ArrayList<SubtitleAndKey>> sectionSubtitles=new HashMap<>();
    private ArrayList<SubtitleData> subtitleDatas=new ArrayList<>();
    private ArrayList<SubtitleAndKey> temp=new ArrayList<>();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private CustomDialog customDialog;
    private TextView subtitlebox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_watch_video);
        filedirectory = this.getCacheDir();

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
        FloatingActionButton fab = findViewById(R.id.make_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_choice();
            }
        });

        /* recycle view(part) */
        recyclerView1 = findViewById(R.id.part_list);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        adapter1 = new RecyclerAdapter(this, this);
        recyclerView1.setAdapter(adapter1);

        final Intent activityData1 = getIntent();
        videoID = activityData1.getExtras().getString("link");
        sectionCount = activityData1.getExtras().getInt("count");

        getSections();

        /* recycle view(subtitles) */
        recyclerView2 = findViewById(R.id.sub_list);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapter2 = new RecyclerAdapterS(this, this);
        recyclerView2.setAdapter(adapter2);

        subtitlebox = (TextView) findViewById(R.id.subtitle);

        /* 동영상 로드 및 초기화 */
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

    private void getData() {
        // 데이터 초기화
        adapter2.delItem();
        // 임의의 데이터입니다. -> 해당 아이디 가져오기로 변경완료
        temp=sectionSubtitles.get(String.valueOf(posi));
        List<String> listName = new ArrayList<>(temp.size());
        List<Boolean> type = new ArrayList<>(temp.size());
        Collections.sort( temp );
        for(SubtitleAndKey tmp:temp){
            listName.add(tmp.name);
            type.add(tmp.type);
        }

        adapter2.delItem();
        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            DataS dataS = new DataS();
            dataS.setName(listName.get(i));
            if(type.get(i)) {
                dataS.setOnSubtitle(true);
                dataS.setOnSound(false);
            }
            else {
                dataS.setOnSubtitle(false);
                dataS.setOnSound(true);
            }

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter2.addItem(dataS);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter2.notifyDataSetChanged();
    }

    public void getSections(){

        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listState.clear();
                for(int i=0;i<sectionCount;i++){
                    listState.add( 0,false );
                }

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                        if(video.getLink().equals( videoID )){
                            idvideo=snapshot.getKey();
                            int view=video.getView();
                            databaseReference.child( idvideo ).child( "view" ).setValue(view+1);
                            for(DataSnapshot subtitleSnap:snapshot.child( "SUBTITLE" ).getChildren()) {
                                ArrayList<SubtitleAndKey> subtitles = new ArrayList<>();
                                for (DataSnapshot temp : subtitleSnap.getChildren()) {
                                    Subtitle subtitle = temp.getValue( Subtitle.class );
                                    SubtitleAndKey subtitleAndKey = new SubtitleAndKey( subtitle, temp.getKey() );
                                    subtitles.add( subtitleAndKey );
                                }
                                String index = subtitleSnap.getKey();
                                sectionSubtitles.put( index, subtitles );
                                int index2 = Integer.parseInt( index );
                                if (index2 != 0)
                                    listState.set( index2 - 1, true );
                            }
                            break;
                        }
                    }
                    setData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void setData() {
        int partNum = listState.size(); // 파트 5개 있다고 가정
        for (int i = 0; i < partNum; i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setNum(i+1);
            data.setState(listState.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter1.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter1.notifyDataSetChanged();
    }

    public void getListienSubtitle(int position) {
        output=new outputDataController();
        subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get(String.valueOf(posi)).get(position).getKey() );
        subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get(String.valueOf(posi)).get(position).getKey() ); //  이 줄 삭제 하면 안됩니다. 큰일 나요. !!

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int[] compare_s = new int[subtitleDatas.size()];
                int[] compare_f = new int[subtitleDatas.size()];
                for(int i = 0 ; i < subtitleDatas.size(); i++){
                    String min = subtitleDatas.get(i).getSectionS().split(":")[0];
                    String sec = subtitleDatas.get(i).getSectionS().split(":")[1];
                    compare_s[i] = (Integer.parseInt(min)*60) + Integer.parseInt(sec);
                    String min1 = subtitleDatas.get(i).getSectionE().split(":")[0];
                    String sec1 = subtitleDatas.get(i).getSectionE().split(":")[1];
                    compare_f[i] = (Integer.parseInt(min1)*60) + Integer.parseInt(sec1);
                }
                while(true) {
                    for(int i=0; i < subtitleDatas.size(); i++) {
                        if((player.getCurrentTimeMillis()/1000) >= compare_s[i] && player.getCurrentTimeMillis()/1000 < compare_f[i]) {
                            subtitlebox.setText(subtitleDatas.get(i).getSubString());
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        th.start();
    }

    void type_choice() {
        //customDialog = new CustomDialog(this, sectionCount);
        //customDialog.show();
        final AlertDialog dialog;
        final CharSequence[] items = {"자막", "소리"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("해설 타입 선택");

        final CharSequence [] type = new String[1];
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                type[0] = items[item];
            }
        });

        final Spinner dropdown = new Spinner(this);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dropdown);
            popupWindow.setHeight(700);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
        dropdown.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String[] options = new String[sectionCount+1];
        options[0]="파트 선택" ;
        for(int i=0;i<sectionCount;i++)
            options[i+1]=String.valueOf( i+1 );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        dropdown.setAdapter(adapter);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(60, 0, 60, 0);
        linearLayout.addView(dropdown);
        builder.setView(linearLayout);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                if( (!TextUtils.equals(type[0],items[0]) && !TextUtils.equals(type[0],items[1])) || dropdown.getSelectedItem().toString().equals("파트 선택")) {
                    Toast.makeText(getApplicationContext(), "선택하지 않은 항목이 있는지 확인해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    wantToCloseDialog = true;
                    Intent intent = new Intent(WatchVideoActivity.this, MakeVideoActivity.class);
                    intent.putExtra("link", videoID);
                    intent.putExtra("type", type[0]);
                    intent.putExtra("part", dropdown.getSelectedItem().toString());
                    startActivity(intent);
                }
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }

    @Override
    public void onItemSelectedS(View v, int position) {
        RecyclerAdapterS.ItemViewHolder viewHolder;
        viewHolder = (RecyclerAdapterS.ItemViewHolder)recyclerView2.findViewHolderForAdapterPosition(position);
        if(v.equals(viewHolder.subtitleButton)) {
            getListienSubtitle(position);
        } else if(v.equals(viewHolder.soundButton)) {
            Toast.makeText(getApplicationContext(), "귀때기"+position, Toast.LENGTH_SHORT).show();
        }
    }

    /* 파트 선택된 거 어둡게 바꿈 */
    @Override
    public void onItemSelected(View v, int position) {
        RecyclerAdapter.ItemViewHolder viewHolder;
        if(prePosition == -1) {
            viewHolder = (RecyclerAdapter.ItemViewHolder)recyclerView1.findViewHolderForAdapterPosition(0);
            viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
        }
        else {
            viewHolder = (RecyclerAdapter.ItemViewHolder)recyclerView1.findViewHolderForAdapterPosition(prePosition);
            viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
        }
        viewHolder = (RecyclerAdapter.ItemViewHolder)recyclerView1.findViewHolderForAdapterPosition(position);
        viewHolder.imageView.setColorFilter(Color.argb(128, 0, 0, 0));
        prePosition = position;

        posi = position+1;

        getData();
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
                intent = new Intent(WatchVideoActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_commentation:
                intent = new Intent(WatchVideoActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                intent = new Intent(WatchVideoActivity.this, VideoRequestListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_my_video:
                intent = new Intent(WatchVideoActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                intent = new Intent(WatchVideoActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_notice:
                intent = new Intent(WatchVideoActivity.this, NoticeActivity.class);
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
