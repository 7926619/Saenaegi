package com.saenaegi.lfree;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
//import android.speech.tts.Voice;
import java.util.Locale;

public class aAccessibilityService extends android.accessibilityservice.AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private TextToSpeech tts;              // TTS 변수 선언

    /*
    @Override
    public void onCreate() {
        // getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
    }
    */

    // 이벤트가 발생할때마다 실행되는 부분
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 발생한 이벤트로부터 Source를 get
        AccessibilityNodeInfo source = event.getSource();
        // 실현 시간 상수로서 접근성 서비스에 대한 이벤트 타입 변수 선언 및 생성
        final int eventType =  event.getEventType();
        // 특정 이벤트에 대한 서술 변수
        String eventText = null;

        if(source == null)
            return;

        switch(eventType) {
            // 특정 컴포넌트에 대해 포커싱이 발생하면
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                eventText = "Focused: ";
                break;
            // 특정 컴포넌트에 대해 사용자의 직접적인 클릭이 발생하면
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Clicked : ";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventText = "Selected : ";
                break;
        }
        // 이벤트의 대상이 된 컴포넌트의 ContentDescription 내용을 String에 저장 및 출력
        eventText = eventText + event.getContentDescription();
        Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();

        // TTS 기능으로 말하기

        // TextToSpeech은 버전에 따라 다르게 구성
        if(eventType != AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        else if(eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("스크롤 중", TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("스크롤 중", TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        //tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null, null);

        /*
        // 발생한 이벤트로부터 소스 get
        AccessibilityNodeInfo nodeInfo = event.getSource();

        // 이벤트를 발생시킨 해당 소스에 대한 Action 실행. 이 때의 Action은 접근성 서비스를 위한 FOCUS
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);

        // 다시 사용할 수 있도록 해당 인스턴스를 반환
        nodeInfo.recycle();

        Log.e(TAG, "Catch Event : " + event.toString());
        Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
        Log.e(TAG, "Catch Event TEXT : " + event.getText());
        Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
        Log.e(TAG, "Catch Event getSource : " + event.getSource());
        Log.e(TAG, "=========================================================================");
        */
    }

    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    @Override
    public void onServiceConnected() {
        //Toast.makeText(getApplication(), "Accessibility Service : Connected", Toast.LENGTH_SHORT).show();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREAN);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplication(), "TTS : Korean Language Not Supported!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplication(), "TTS : TTS's Initialization is Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // 새내기 애플리케이션에서만 접근성 서비스가 동작 시작하도록 설정. 물론 동작 시작된 서비스는 다른 앱에서도 동작되게 됨.
        info.packageNames = new String[]{"com.saenaegi.lfree"};

        // 뷰 클릭 시, 뷰 포커싱 시, 제스처 인식 시, 검색창 텍스트 변경 시 접근성 서비스 이용을 위한 이벤트 타입 수집
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED
                | AccessibilityEvent.TYPE_GESTURE_DETECTION_START | AccessibilityEvent.TYPE_GESTURE_DETECTION_END
                | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED;
        //info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기

        // 이벤트 발생 시 음성 피드백 제공
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        //info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;

        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        //info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FINGERPRINT_GESTURES;
        info.notificationTimeout = 10; // millisecond

        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        //Log.e("TEST", "OnInterrupt");
        // 특정 컴포넌트에 대한 tts 기능 사용 후에는 반드시 shutdown을 시켜 리소스 낭비를 피하도록 한다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void onUnbind() {

    }
}