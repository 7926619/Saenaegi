package com.saenaegi.lfree;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.saenaegi.lfree.SubtitleController.DeleteDataController;
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;
import com.saenaegi.lfree.SubtitleController.outputDataController;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private String userid="userid";
    private int sectionCount;
    private File filedirectory;
    private outputDataController output;
    private ArrayList<Boolean> listState=new ArrayList<>();
    private HashMap<String, ArrayList<SubtitleAndKey>> sectionSubtitles=new HashMap<>();
    private ArrayList<SubtitleData> subtitleDatas=new ArrayList<>();
    private ArrayList<SubtitleAndKey> temp=new ArrayList<>();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private DatabaseReference databaseReference2=firebaseDatabase.getReference().child( "LFREE" ).child( "LIKEVIDEO" ).child( userid );
    private ArrayList<String> likesubtitleKey=new ArrayList<>();
    private CustomDialog customDialog;
    private TextView subtitlebox;
    private int flag = 1;
    private int flag2 = 1;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_watch_video);
        filedirectory = this.getCacheDir();

        /* scroll on top */
        final ScrollView scroll_view = findViewById(R.id.scroll_view);
        scroll_view.post(new Runnable() {
            public void run() {
                scroll_view.scrollTo(0, 0);
            }
        });

        /* Action Bar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* navigation */
        drawerLayout = findViewById(R.id.drawerLayout);

        final ImageButton drawerButton = findViewById(R.id.drawer_icon);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        /* youtube_screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_height = size.x * 9 / 16;    // 16:9 비율

        View youtube_screen = findViewById(R.id.youtube_screen);
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
        getLikeVideo();
        /* recycle view(subtitles) */
        recyclerView2 = findViewById(R.id.sub_list);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapter2 = new RecyclerAdapterS(this, this);
        recyclerView2.setAdapter(adapter2);

        subtitlebox = findViewById(R.id.subtitle);

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

            /* footer */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.footer, new FooterFragment());
        fragmentTransaction.commit();
        }

    public void getLikeVideo(){
        databaseReference2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likesubtitleKey.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals( idvideo )){
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            String temp=snapshot1.getKey();
                            likesubtitleKey.add( temp );
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
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
            dataS.setOnMore(true);
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
    public void setSubtitleDatas(int position, boolean type){
        output=new outputDataController();
        if(type) {
            subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() );
            subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() ); //  이 줄 삭제 하면 안됩니다. 큰일 나요. !! --> 가끔 파일이 생성 안되어 안 받아 올때를 대비
        }
        else{
            subtitleDatas = output.getLookSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() );
            subtitleDatas = output.getLookSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() ); //  이 줄 삭제 하면 안됩니다. 큰일 나요. !! --> 가끔 파일이 생성 안되어 안 받아 올때를 대비
        }
    }

    public void getListenSubtitle(int position) {
        output=new outputDataController();
        subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() );
        subtitleDatas = output.getListenSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() ); //  이 줄 삭제 하면 안됩니다. 큰일 나요. !! --> 가끔 파일이 생성 안되어 안 받아 올때를 대비

        String min = sectionSubtitles.get(String.valueOf(posi)).get(position).getSubtitle().getSectionS().split(":")[0];
        String sec = sectionSubtitles.get(String.valueOf(posi)).get(position).getSubtitle().getSectionS().split(":")[1];
        int compare = ((Integer.parseInt(min)*60) + Integer.parseInt(sec))*1000;

        player.seekToMillis(compare);
        flag = 0;
        flag2 = 0;
        Thread th = new Thread(r);
        th.start();
    }

    public void getLookSubtitle(int position) {
        output=new outputDataController();
        subtitleDatas = output.getLookSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() );
        subtitleDatas = output.getLookSubtitleData( filedirectory, posi, idvideo, sectionSubtitles.get( String.valueOf( posi ) ).get( position ).getKey() ); //  이 줄 삭제 하면 안됩니다. 큰일 나요. !! --> 가끔 파일이 생성 안되어 안 받아 올때를 대비

        String min = sectionSubtitles.get(String.valueOf(posi)).get(position).getSubtitle().getSectionS().split(":")[0];
        String sec = sectionSubtitles.get(String.valueOf(posi)).get(position).getSubtitle().getSectionS().split(":")[1];
        int compare = ((Integer.parseInt(min)*60) + Integer.parseInt(sec))*1000;

        player.seekToMillis(compare);
        flag = 0;
        flag2 = 0;
        Thread th2 = new Thread(r2);
        th2.start();
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
                flag = -1;
                while(flag2==0) {
                }
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
    public void onItemSelectedS(View v, final int position) {
        RecyclerAdapterS.ItemViewHolder viewHolder;
        viewHolder = (RecyclerAdapterS.ItemViewHolder)recyclerView2.findViewHolderForAdapterPosition(position);

        if(v.equals(viewHolder.subtitleButton)) {
            if(flag == 0) {
                Log.e("1","1");
                flag = -1;
                while(flag2==0) {
                    Log.e("flag",""+flag);
                    Log.e("2","2");
                }
                Log.e("3","3");
                getListenSubtitle(position);
            }
            else {
                Log.e("4","4");
                getListenSubtitle(position);
            }
        } else if(v.equals(viewHolder.soundButton)) {
            subtitlebox.setText("");
            if(flag == 0) {
                Log.e("5","5");
                flag = -1;
                while(flag2==0) {
                    Log.e("flag",""+flag);
                    Log.e("6","6");
                }
                Log.e("7","7");
                getLookSubtitle(position);
            }
            else {
                Log.e("8","8");
                getLookSubtitle(position);
            }
        } else if(v.equals( viewHolder.moreButton )){
            final SubtitleAndKey subtitleAndKey=temp.get( position );
            final Subtitle temp=subtitleAndKey.getSubtitle();
            View view1 = v;
            PopupMenu p = new PopupMenu(viewHolder.moreButton.getContext(), v);
            p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final String key=subtitleAndKey.getKey();
                    final int recommend=subtitleAndKey.getRecommend();
                    final String makeUserid=temp.getIdgoogle();
                    final boolean type=temp.isType();
                    switch (item.getItemId()) {
                        case R.id.opt1:
                            boolean exist=false;
                            for(String temp:likesubtitleKey) {
                                if (temp.equals( key )) {
                                    Toast.makeText( getApplicationContext(), "한 번 추천한 영상은 다시 추천할 수 없습니다.", Toast.LENGTH_SHORT ).show();
                                    exist = true;
                                    break;
                                }
                            }
                             if(!exist){
                                 likesubtitleKey.add( key );
                                 databaseReference2.child( idvideo ).child( key ).setValue( "idsubtitle" );
                                 databaseReference.child( idvideo ).child( "SUBTITLE" ).child( String.valueOf( posi ) ).child( key ).child( "recommend" ).setValue( recommend + 1 );
                                 Toast.makeText( getApplicationContext(), "추천되었습니다.", Toast.LENGTH_SHORT ).show();
                             }
                            break;
                        case R.id.opt2:
                            DatabaseReference dataRef=firebaseDatabase.getReference().child( "LFREE" ).child( "DECLARATION" ).child( key );
                            dataRef.child( userid ).setValue( "신고자 id" );
                            Toast.makeText(getApplicationContext(), "신고되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.opt3:
                            if(userid.equals( makeUserid )){
                                SubtitleAndKey temp=sectionSubtitles.get(String.valueOf( posi )).get(position);
                                setSubtitleDatas( position,temp.type);
                                Intent intent = new Intent(WatchVideoActivity.this, MakeVideoActivity.class);
                                intent.putExtra("link", videoID);
                                intent.putExtra("type", temp.type);
                                intent.putExtra("part", posi);
                                intent.putExtra( "modify",true );
                                intent.putExtra( "idsubtitle",temp.getKey() );
                                intent.putParcelableArrayListExtra( "subtitles",subtitleDatas);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "제작자가 아니면, 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.opt4:
                            if(userid.equals( makeUserid )){
                                DeleteDataController dataController=new DeleteDataController(idvideo,key,posi,type);
                                dataController.deleteData();
                                sectionSubtitles.get( String.valueOf( posi ) ).remove( position );
                                adapter2.removeItem( position );
                                adapter2.notifyDataSetChanged();

                                if(sectionSubtitles.get( String.valueOf( posi )).isEmpty()){
                                    adapter1.delItem(posi);
                                    adapter1.notifyDataSetChanged();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "제작자가 아니면 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                    return false;
                }
            });
            // here you can inflate your menu
            p.inflate(R.menu.subtitle_menu);
            p.setGravity(Gravity.RIGHT);
            p.show();
        }

}


    /* 파트 선택된 거 어둡게 바꿈 */
    @Override
    public void onItemSelected(View v, int position) {
        RecyclerAdapter.ItemViewHolder viewHolder;
        if(prePosition == -1) {
            TextView partText = findViewById(R.id.partText);
            partText.setVisibility(View.GONE);
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
                tts.shutdown();
                flag = -1;
                while(flag2==0) {
                }
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
                flag = -1;
                tts.shutdown();
                ActivityCompat.finishAffinity(this);
            }
        }
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            int[] compare_s = new int[subtitleDatas.size()];
            int[] compare_f = new int[subtitleDatas.size()];
            for(int i = 0 ; i < subtitleDatas.size(); i++){
                String min1 = subtitleDatas.get(i).getSectionS().split(":")[0];
                String sec1 = subtitleDatas.get(i).getSectionS().split(":")[1];
                compare_s[i] = (Integer.parseInt(min1)*60) + Integer.parseInt(sec1);
                String min2 = subtitleDatas.get(i).getSectionE().split(":")[0];
                String sec2 = subtitleDatas.get(i).getSectionE().split(":")[1];
                compare_f[i] = (Integer.parseInt(min2)*60) + Integer.parseInt(sec2);
            }
            breakpoint:
            while(true) {
                for(int i=0; i < subtitleDatas.size(); i++) {
                    if(flag == -1) {
                        flag2 = -1;
                        break breakpoint;
                    }
                    if((player.getCurrentTimeMillis()/1000) >= compare_s[i] && (player.getCurrentTimeMillis()/1000) < compare_f[i]) {
                        subtitlebox.setText(subtitleDatas.get(i).getSubString());
                        while((player.getCurrentTimeMillis()/1000) >= compare_s[i] && (player.getCurrentTimeMillis()/1000) < compare_f[i]) {
                            if(flag == -1) {
                                flag2 = -1;
                                break breakpoint;
                            }
                        }
                    }
                    else
                        subtitlebox.setText("");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            flag2 = -1;
        }
    };

    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            int[] compare_s = new int[subtitleDatas.size()];
            int[] compare_f = new int[subtitleDatas.size()];
            for(int i = 0 ; i < subtitleDatas.size(); i++){
                String min1 = subtitleDatas.get(i).getSectionS().split(":")[0];
                String sec1 = subtitleDatas.get(i).getSectionS().split(":")[1];
                compare_s[i] = (Integer.parseInt(min1)*60) + Integer.parseInt(sec1);
                String min2 = subtitleDatas.get(i).getSectionE().split(":")[0];
                String sec2 = subtitleDatas.get(i).getSectionE().split(":")[1];
                compare_f[i] = (Integer.parseInt(min2)*60) + Integer.parseInt(sec2);
            }
            breakpoint:
            while(true) {
                for(int i=0; i < subtitleDatas.size(); i++) {
                    if(flag == -1) {
                        tts.stop();
                        flag2 = -1;
                        break breakpoint;
                    }
                    if((player.getCurrentTimeMillis()/1000) >= compare_s[i] && (player.getCurrentTimeMillis()/1000) < compare_f[i]) {
                        tts.setSpeechRate((float)0.87);
                        tts.speak(subtitleDatas.get(i).getSubString(), TextToSpeech.QUEUE_FLUSH, null);
                        while((player.getCurrentTimeMillis()/1000) >= compare_s[i] && (player.getCurrentTimeMillis()/1000) < compare_f[i]) {
                            if(flag == -1) {
                                tts.stop();
                                flag2 = -1;
                                break breakpoint;
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            tts.stop();
            flag2 = -1;
        }
    };
}
