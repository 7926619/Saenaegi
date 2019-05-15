package com.saenaegi.lfree;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
//import android.speech.tts.Voice;
import java.util.Locale;

public class aAccessibilityService extends AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private TextToSpeech tts;              // TTS 변수 선언


    @Override
    public void onCreate() {
        // getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        /*
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
        */
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
    }


    // 이벤트가 발생할때마다 실행되는 부분
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "=========================================================================");
        Log.e(TAG, "Catch Event : " + event.toString());
        Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
        Log.e(TAG, "Catch Event TEXT : " + event.getText());
        Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
        Log.e(TAG, "Catch Event getSource : " + event.getSource());
        Log.e(TAG, "=========================================================================");
        // 발생한 이벤트로부터 Source를 get
        AccessibilityNodeInfo source = event.getSource();
        // 실현 시간 상수로서 접근성 서비스에 대한 이벤트 타입 변수 선언 및 생성
        final int eventType =  event.getEventType();
        // 특정 이벤트에 대한 서술 변수
        String eventText = null;

        // 접근성 이벤트의 소스가 null이라면 예외가 발생한 것이므로 Side Effect가 발생하기 전에 바로 return
        if(source == null) {
            Toast.makeText(getApplicationContext(), "Accessibility Service Source is NULL!", Toast.LENGTH_LONG).show();
            return;
        }

        // 제스처 발생 시에는 해당 제스처에 대한 기능을 먼저 수행토록 하고, TTS 기능은 사용되지 않도록 유도
        if(eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_START || eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_END) {
            source.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
            source.recycle();
        }

        else{
            switch (eventType) {
                // 특정 컴포넌트에 대해 접근성 관련 포커싱이 발생하면
                case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                    eventText = "Accessibility Focused : ";
                    break;
                // 특정 컴포넌트에 대해 사용자의 직접적인 클릭이 발생하면
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    eventText = "Clicked : ";
                    break;
                case AccessibilityEvent.TYPE_VIEW_SELECTED:         // AdapterView에 적재되어 있는 특정 갯수의 아이템이 선택될 시
                    eventText = "Selected : ";
                    break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                    eventText = "Focused : ";
                    break;
            }
            // 이벤트의 대상이 된 컴포넌트의 ContentDescription 내용을 String에 저장 및 출력
            eventText = eventText + event.getContentDescription();
            Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();

            // TTS 기능으로 말하기

            // TextToSpeech은 버전에 따라 다르게 구성
            if (eventType != AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
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
    }

    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    @Override
    public void onServiceConnected() {
        Toast.makeText(getApplication(), "LFREE Accessibility Service : Connected!", Toast.LENGTH_LONG).show();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // 새내기 애플리케이션에서만 접근성 서비스가 동작 시작하도록 설정. 물론 동작 시작된 서비스는 다른 앱에서도 동작되게 됨.
        info.packageNames = new String[]{"com.saenaegi.lfree"};

        // 뷰 클릭 시, 뷰 포커싱 시, 제스처 인식 시, 검색창 텍스트 변경 시 접근성 서비스 이용을 위한 이벤트 타입 수집

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED
                | AccessibilityEvent.TYPE_GESTURE_DETECTION_START | AccessibilityEvent.TYPE_GESTURE_DETECTION_END
                | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
                | AccessibilityEvent.TYPE_VIEW_SELECTED | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED;

        //info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기

        // 이벤트 발생 시 음성 피드백 제공
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        //info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        //info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        // 제스처 기능 수행을 위한 터치 모드 수행 플래그 설정
        //info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        //info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        //info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FINGERPRINT_GESTURES;
        info.notificationTimeout = 100; // millisecond
        // 서비스 설정
        setServiceInfo(info);

    }

    @Override
    public void onInterrupt() {
        //Log.e("TEST", "OnInterrupt");
        // 특정 컴포넌트에 대한 tts 기능 사용 후에는 반드시 shutdown을 시켜 리소스 낭비를 피하도록 한다.
        // 이 때 TTS 기능이 shutdown이 되더라도 다시 enable하면 되므로 상관없다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void onUnbind() {

    }
}