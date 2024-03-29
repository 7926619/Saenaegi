package com.saenaegi.lfree;

import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.saenaegi.lfree.Data.Subtitle;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class aWatchVideoActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youtubeScreen;
    private TextView play, pause, next, pre;
    private YouTubePlayer.OnInitializedListener listener;
    private YouTubePlayer player;
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
    private DatabaseReference databaseReference3=firebaseDatabase.getReference().child( "LFREE" );
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference look=storage.getReference().child( "LookSubtitle" );
    private ArrayList<String> likesubtitleKey=new ArrayList<>();
    private TextToSpeech tts;
    private int flag=1,flag2=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
        setContentView( R.layout.activity_a_watch_video );

        //TextView gesturetext = (TextView)findViewById(R.id.gesture);
        //gesturetext.setLayoutParams(new RelativeLayout.LayoutParams(50,50));


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
        madesection=new String[sectionCount+1];

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
                if (player != null) {
                    if (player.isPlaying())
                        player.pause();
                }
                if(sectionSubtitles.size()!=0) {
                    tts.shutdown();
                    Intent intent = new Intent( aWatchVideoActivity.this, aSelectPartActivity.class );
                    intent.putExtra( "link", videoID );
                    intent.putExtra( "count", sectionCount );
                    intent.putExtra( "madesection", madesection );
                    flag = -1;
                    while(flag2==0) {
                    }
                    startActivity( intent );
                    finish();
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
                    Log.e( "position",String.valueOf(position) );
                    if(flag == 0) {
                        Log.e("1","1");
                        flag = -1;
                        while(flag2==0) {
                            Log.e("flag",""+flag);
                            Log.e("2","2");
                        }
                        Log.e("3","3");
                        getLookSubtitleData();
                    }
                    else {
                        Log.e("4","4");
                        getLookSubtitleData();
                    }
                }else {
                    String eventText = "다른 해설 재생 버튼 클릭 : 파트를 먼저 선택하여 주세요";
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
                    else{
                        String eventText = "이미 추천한 영상입니다.";
                        Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                        tts.setSpeechRate((float)0.87);
                        tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
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
                    DatabaseReference dataRef = databaseReference3.child( "DECLARATION" ).child( key );
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
                            if(subtitles.size()!=0&&Integer.parseInt( index )!=0) {
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
                if(nowSection!=0){
                    Toast.makeText(getApplication(), "자막읽자읽자읽자!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                    if(flag == 0) {
                        Log.e("1","1");
                        flag = -1;
                        while(flag2==0) {
                            Log.e("flag",""+flag);
                            Log.e("2","2");
                        }
                        Log.e("3","3");
                        getLookSubtitleData();
                    }
                    else {
                        Log.e("4","4");
                        getLookSubtitleData();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    public void getLookSubtitleData() {
        subtitleDatas.clear();
        String key=sectionSubtitles.get(String.valueOf(nowSection)).get(position).getKey();
        String filename=key+".txt";
        StorageReference islandRef=look.child(idvideo).child(String.valueOf( nowSection)).child(filename);
        try {
            final File localFile = File.createTempFile( key, "txt" );
            StorageTask<FileDownloadTask.TaskSnapshot> taskSnapshotStorageTask = islandRef.getFile( localFile );
            taskSnapshotStorageTask.addOnSuccessListener( new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    //Log.e( "chedkFileReader"," read file success" );
                    try {
                        FileReader fileReader = new FileReader( localFile );
                        BufferedReader bufferedReader = new BufferedReader( fileReader );
                        String onLine = null;
                        while ((onLine = bufferedReader.readLine()) != null) {
                            //Log.e( "chedkFileReader", "in of while" );
                            String[] arr = onLine.split( "\t" );
                            SubtitleData subtitleData = new SubtitleData( arr[0].trim(), arr[1].trim(), arr[2].trim() );
                            subtitleDatas.add( subtitleData );
                        }
                        Log.e( "chedkFileReader", String.valueOf( subtitleDatas.size() ) );
                        fileReader.close();
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e( "chedkFileReader"," success file not exist" );
                    }

                    String min = sectionSubtitles.get( String.valueOf( nowSection ) ).get( position ).getSubtitle().getSectionS().split( ":" )[0];
                    String sec = sectionSubtitles.get( String.valueOf( nowSection ) ).get( position ).getSubtitle().getSectionS().split( ":" )[1];
                    int compare = ((Integer.parseInt( min ) * 60) + Integer.parseInt( sec )) * 1000;
                    if(flag == 1)
                        player.seekToMillis( compare );
                    flag = 0;
                    flag2 = 0;
                    Thread th2 = new Thread( r2 );
                    th2.start();

                        //자막 재생 하는 부분
                }

            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    //Log.e( "chedkFileReader"," read file false" );
                }
            } );
        } catch (IOException e){
            Log.e( "chedkFileReader"," not make file" );
        }


    }

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
                        android.util.Log.d(null, "==================> " + e.toString());
                    }
                }
            }
            tts.stop();
            flag2 = -1;
        }
    };
}
