package com.saenaegi.lfree;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

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
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    private HashMap<String, ArrayList<SubtitleAndKey>> sectionSubtitles = new HashMap<>();
    private ArrayList<SubtitleData> subtitleDatas = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private outputDataController output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
        setContentView( R.layout.activity_a_watch_video );
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
        nowSection = data.getExtras().getInt( "nowSection" );


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

        /* youtube_screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize( size );
        int screen_height = size.x * 9 / 16;    // 16:9 비율

        View youtube_screen = (View) findViewById( R.id.youtube_screen );
        ViewGroup.LayoutParams params = youtube_screen.getLayoutParams();
        params.height = screen_height;
        youtube_screen.setLayoutParams( params );

        TextView tv1 = (TextView) findViewById( R.id.textView22 );
        tv1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( aWatchVideoActivity.this, aSelectPartActivity.class );
                intent.putExtra( "link", videoID );
                intent.putExtra( "count", sectionCount );
                startActivity( intent );
            }
        } );
        getData();
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
                            sectionSubtitles.put( index, subtitles );
                        }
                        break;
                    }

                }
                readingSubtitle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void readingSubtitle(){

        output=new outputDataController();
        ArrayList<SubtitleAndKey> temp=sectionSubtitles.get( String.valueOf( nowSection));
        subtitleDatas = output.getLookSubtitleData( filedirectory, nowSection, idvideo, "-Lg6q60mYea5eRmHxSsd" );
        // cash error로 인하여
        subtitleDatas = output.getLookSubtitleData( filedirectory, nowSection, idvideo, "-Lg6q60mYea5eRmHxSsd");

    }

}
