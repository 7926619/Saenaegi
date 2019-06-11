package com.saenaegi.lfree;

import android.content.Context;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;
import com.saenaegi.lfree.SubtitleController.outputDataController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class aWatchVideoActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youtubeScreen;
    private TextView play, pause, next, pre;
    private YouTubePlayer.OnInitializedListener listener;
    private YouTubePlayer player;
    private File filedirectory;
    private String videoID;
    private String idvideo;
    private int sectionCount;
    private int nowSection;
    private int position=0;
    private String userid="userid";
    private HashMap<String, ArrayList<SubtitleAndKey>> sectionSubtitles = new HashMap<>();
    private String[] madesection;
    private ArrayList<SubtitleData> subtitleDatas = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private DatabaseReference databaseReference2=firebaseDatabase.getReference().child( "LFREE" ).child( "LIKEVIDEO" ).child( userid );
    private ArrayList<String> likesubtitleKey=new ArrayList<>();
    private outputDataController output;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
        setContentView( R.layout.activity_a_watch_video );

        //TextView gesturetext = (TextView)findViewById(R.id.gesture);
        //gesturetext.setLayoutParams(new RelativeLayout.LayoutParams(50,50));

        filedirectory=this.getCacheDir();

        /* youtube용 변수 */
        play = (TextView) findViewById( R.id.textView22 );
        pause = (TextView) findViewById( R.id.textView19 );
        next = (TextView) findViewById( R.id.textView20 );
        pre = (TextView) findViewById( R.id.textView21 );
        youtubeScreen = (YouTubePlayerView) findViewById( R.id.youtube_screen );

        final Intent data = getIntent();
        videoID = data.getExtras().getString( "link" );
        sectionCount = data.getExtras().getInt( "count" );
        nowSection = data.getExtras().getInt( "nowSection" );   //nowSection이 잘 넘어오는 지 확인
        madesection=new String[sectionCount];

        /* 동영상 로드 및 초기화 */
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.cueVideo( videoID );

                player.setPlayerStateChangeListener( new YouTubePlayer.PlayerStateChangeListener() {
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
                } );
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        youtubeScreen.initialize( "AIzaSyAn_HFubCwx1rbM2q45hMGGhCPUx2AEOz4", listener );

        /*각종 버튼 함수*/
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 재생버튼
                if (player != null) {
                    if (player.isPlaying() == false)
                        player.play();
                }
            }
        } );

        pause.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 정지버튼
                if (player != null) {
                    if (player.isPlaying())
                        player.pause();
                }
            }
        } );

        pre.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 뒤로 10초
                player.seekRelativeMillis( -10000 );
            }
        } );

        next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 다음 10초
                player.seekRelativeMillis( 10000 );
            }
        } );
        /* 전자친구 */
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage( Locale.KOREAN);
                }
                else
                    Toast.makeText(getApplication(), "TTS : TTS's Initialization is Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        /* youtube_screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize( size );
        int screen_height = size.x * 9 / 16;    // 16:9 비율

        View youtube_screen = (View) findViewById( R.id.youtube_screen );
        ViewGroup.LayoutParams params = youtube_screen.getLayoutParams();
        params.height = screen_height;
        youtube_screen.setLayoutParams( params );

        TextView tv1 = (TextView) findViewById( R.id.textView16 );
        tv1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if(sectionSubtitles.size()!=0) {
                    Intent intent = new Intent( aWatchVideoActivity.this, aSelectPartActivity.class );
                    intent.putExtra( "link", videoID );
                    intent.putExtra( "count", sectionCount );
                    intent.putExtra( "madesection", madesection );
                    startActivity( intent );
                }
                else{
                    String eventText = "파트 선택 버튼 클릭 : 이 영상은 아직 미제작 되었습니다.";
                    Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                    tts.setSpeechRate((float)0.87);
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } );

        TextView tv2 = (TextView) findViewById( R.id.textView );
        tv2.setOnClickListener( new View.OnClickListener() {
            //조회수가 가장 높은 영상부터 차례차례 읽다가 더이상 읽을 영상이 없으면 다시 조회수가 가장 높은 영상으로 돌아간다.
            public void onClick(View v) {
                if(nowSection!=0&&sectionSubtitles.size()!=0) {
                    position = position + 1;
                    if (position >= sectionSubtitles.get( String.valueOf( nowSection ) ).size()) {
                        position = 0;
                    }
                    readingSubtitle();
                }else {
                    String eventText = "같은 파트에 다른 것 선택 버튼 클릭 : 파트를 먼저 선택하여 주세요";
                    Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                    tts.setSpeechRate((float)0.87);
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } );

        TextView tv3 = (TextView) findViewById( R.id.textView15 );
        tv3.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if(nowSection!=0) {
                    //제대로 되는 지 확인
                    SubtitleAndKey subtitleAndKey = sectionSubtitles.get( String.valueOf( nowSection )).get( position ); //몇 번째 자막을 선택 하느냐,
                    final String key = subtitleAndKey.getKey();
                    final int recommend = subtitleAndKey.getRecommend();
                    boolean exist = false;
                    for (String tmp : likesubtitleKey) {
                        if (tmp.equals( key )) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        likesubtitleKey.add( key );
                        databaseReference2.child( idvideo ).child( key ).setValue( "idsubtitle" );
                        databaseReference.child( idvideo ).child( "SUBTITLE" ).child( String.valueOf( nowSection ) ).child( key ).child( "recommend" ).setValue( recommend + 1 );
                    }
                }
                else{
                    String eventText = "추천하기 버튼 클릭 : 파트를 선택해야만 추천이 가능합니다.";
                    Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                    tts.setSpeechRate((float)0.87);
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } );

        TextView tv4 = (TextView) findViewById( R.id.textView5 );
        tv4.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if(nowSection!=0) {
                    SubtitleAndKey subtitleAndKey = sectionSubtitles.get( String.valueOf( nowSection ) ).get( position );
                    Subtitle temp = subtitleAndKey.getSubtitle();
                    final String key = subtitleAndKey.getKey();
                    DatabaseReference dataRef = databaseReference2.child( "DECLARATION" ).child( key );
                    dataRef.child( userid ).setValue( "신고자 id" );
                }
                else{
                    String eventText = "신고하기 버튼 클릭 : 파트를 선택해야만 신고가 가능합니다.";
                    Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                    tts.setSpeechRate((float)0.87);
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } );

        getData();
        getLikeVideo();
        if(nowSection!=0&&sectionSubtitles.size()!=0){
            Toast.makeText(getApplication(), "자막읽자읽자읽자!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
            readingSubtitle();
        }
    }

    public  void getLikeVideo(){
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
    public void getData() {
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Video video = snapshot.getValue( Video.class );
                    if (video.getLink().equals( videoID )) {
                        idvideo = snapshot.getKey();
                        int view = video.getView();
                        databaseReference.child( idvideo ).child( "view" ).setValue( view + 1 );
                        for (DataSnapshot subtitleSnap : snapshot.child( "SUBTITLE" ).getChildren()) {
                            ArrayList<SubtitleAndKey> subtitles = new ArrayList<>();
                            for (DataSnapshot temp : subtitleSnap.getChildren()) {
                                Subtitle subtitle = temp.getValue( Subtitle.class );
                                if (!subtitle.isType()) {
                                    SubtitleAndKey subtitleAndKey = new SubtitleAndKey( subtitle, temp.getKey() );
                                    subtitles.add( subtitleAndKey );
                                }
                            }
                            String index = subtitleSnap.getKey();
                            if(subtitles.size()!=0) {
                                Collections.sort( subtitles );
                                sectionSubtitles.put( index, subtitles );
                            }
                        }
                        break;
                    }

                }
                //type이 false인 요소들만 저장이 되는가?
                int i=0;
                for(String temp:sectionSubtitles.keySet()){
                    madesection[i]=temp;
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void readingSubtitle(){

        output=new outputDataController();
        ArrayList<SubtitleAndKey> temp=sectionSubtitles.get( String.valueOf( nowSection));
        String key=temp.get( position ).getKey();
        subtitleDatas = output.getLookSubtitleData( filedirectory, nowSection, idvideo, key );
        // cash error로 인하여
        subtitleDatas = output.getLookSubtitleData( filedirectory, nowSection, idvideo, key); //삭제 하면 은영이 울어요.

        //이제 데이터를 TTS로 유투브 시간에 맞게 뽑아줘용

    }

}
