package com.saenaegi.lfree;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class aAccessibilityService extends android.accessibilityservice.AccessibilityService {
    private static final String TAG = "AccessibilityService";

    @Override
    public void onCreate() {
        getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
    }

    // 이벤트가 발생할때마다 실행되는 부분
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 발생한 이벤트로부터 소스 get
        AccessibilityNodeInfo nodeInfo = event.getSource();

        // 이벤트를 발생시킨 해당 소스에 대한 Action 실행. 이 때의 Action은 접근성 서비스를 위한 FOCUS
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);

        // 다시 사용할 수 있도록 해당 인스턴스를 반환
        nodeInfo.recycle();

        // Log.e(TAG, "Catch Event : " + event.toString());
        // Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
        // Log.e(TAG, "Catch Event TEXT : " + event.getText());
        // Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
        // Log.e(TAG, "Catch Event getSource : " + event.getSource());
        // Log.e(TAG, "=========================================================================");
    }

    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    @Override
    public void onServiceConnected() {

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
        info.notificationTimeout = 100; // millisecond

        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.e("TEST", "OnInterrupt");
    }

    public void onUnbind() {

    }
}