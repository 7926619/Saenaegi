package com.saenaegi.lfree;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.saenaegi.lfree.Data.Video;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class aSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent i;
    private SpeechRecognizer mRecognizer;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );
    private ArrayList<Video> videos=new ArrayList<>();

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},1);
        }

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

        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_search);
        findViewById(R.id.imageView4).setOnClickListener(this);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        getData();
    }

    public void getData(){
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                    videos.add( video );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    public void onClick(View v) {
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
    }

    private RecognitionListener listener = new RecognitionListener() {
        //입력 소리 변경 시
        @Override public void onRmsChanged(float rmsdB) {
        }

        //음성 인식 결과 받음
        @Override public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            Intent intent = new Intent(aSearchActivity.this, aSearchVideoActivity.class);
            intent.putExtra( "search" , rs[0]);
            String eventText = "음성 인식된 단어 : " + rs[0];
            Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
            tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
            startActivity(intent);
        }
        //음성 인식 준비가 되었으면
        @Override public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        //음성 입력이 끝났으면
        @Override public void onEndOfSpeech() {
        }

        //에러가 발생하면
        @Override public void onError(int error) { //전 activity로 에러코드 전송
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "관련 퍼미션 적용 필요";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "관련 단어 탐색 불가";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "Busy RECOGNIZER";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 문제";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "음성인식 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }

            String mes = "음성 인식 오류 발생 : " + message;

            Toast.makeText(getApplicationContext(), mes ,Toast.LENGTH_SHORT).show();
            tts.speak(mes, TextToSpeech.QUEUE_FLUSH, null);

        }

        @Override public void onBeginningOfSpeech() {}							//입력이 시작되면
        @Override public void onPartialResults(Bundle partialResults) {}		//인식 결과의 일부가 유효할 때
        @Override public void onEvent(int eventType, Bundle params) {}		//미래의 이벤트를 추가하기 위해 미리 예약되어진 함수
        @Override public void onBufferReceived(byte[] buffer) {}				//더 많은 소리를 받을 때
    };
}
