package com.saenaegi.lfree;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class aWatchVideoActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youtubeScreen;
    private TextView play, pause, next, pre;
    private YouTubePlayer.OnInitializedListener listener;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_watch_video);

        /* youtube용 변수 */
        play = (TextView) findViewById(R.id.textView22);
        pause = (TextView) findViewById(R.id.textView19);
        next = (TextView) findViewById(R.id.textView20);
        pre = (TextView) findViewById(R.id.textView21);
        youtubeScreen = (YouTubePlayerView)findViewById(R.id.youtube_screen);

        /* 동영상 로드 및 초기화 */
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.cueVideo("bw5Dy_0EmuE");

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
        };
        youtubeScreen.initialize("AIzaSyAn_HFubCwx1rbM2q45hMGGhCPUx2AEOz4", listener);

        /*각종 버튼 함수*/
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {   // 재생버튼
                if(player!=null){
                    if(player.isPlaying()==false)
                        player.play();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {   // 정지버튼
                if(player!=null){
                    if(player.isPlaying())
                        player.pause();
                }
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 뒤로 10초
                player.seekRelativeMillis(-10000);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 다음 10초
                player.seekRelativeMillis(10000);
            }
        });

        /* youtube_screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_height = size.x * 9 / 16;    // 16:9 비율

        View youtube_screen = (View) findViewById(R.id.youtube_screen);
        ViewGroup.LayoutParams params = youtube_screen.getLayoutParams();
        params.height = screen_height;
        youtube_screen.setLayoutParams(params);
    }
}
