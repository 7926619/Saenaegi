package com.saenaegi.lfree;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.speech.tts.TextToSpeech;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;

public class aAccessibilityService extends AccessibilityService {
    private static final String TAG = "AccessibilityService";
    private TextToSpeech tts;              // TTS 변수 선언

    AccessibilityNodeInfo source;
    AccessibilityNodeInfo temp;

    int listviewitemposition = 0;
    int listviewitemposition2 = 0;
    int listviewitemposition3 = 0;
    int listviewitemposition4 = 0;
    int listviewitemposition5 = 0;

    int timercount = 0;
    int timercount2 = 0;
    int timercount3 = 0;
    int timercount4 = 0;
    int timercount5 = 0;

    Handler mHandler = null;
    Handler mHandler2 = null;
    Handler mHandler3 = null;
    Handler mHandler4 = null;
    Handler mHandler5 = null;

    ArrayList<View> arr = new ArrayList<>();
    ArrayList<View> arr2 = new ArrayList<>();

    @Override
    public void onCreate() {
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


    public void listViewAutoFocusDown() {
        if(source.getContentDescription().equals("영상 목록")) {
            ArrayList<TextView> rowtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowList;
            ArrayList<TextView> rowaftertemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowafterList;
            ListView listviewtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(rowtemp == null || rowaftertemp == null)
                return;

            else if(rowtemp.size() == 1) {
                temp = rowtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition = focusposition;
                return;
            }

            temp = rowtemp.get(0);
            for(int j = 0 ; j < rowaftertemp.size() ; j++) {
                if(temp.getContentDescription().equals(rowaftertemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = 0;
                    listviewitemposition = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("파트 선택")) {
            ArrayList<TextView> sectiontemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionList;
            ArrayList<TextView> sectionaftertemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionafterList;
            ListView listviewtemp = ((aSelectPartActivity)aSelectPartActivity.context).listView;
            TextView temp;
            int focusposition;

            if(sectiontemp == null || sectionaftertemp == null)
                return;

            else if(sectiontemp.size() == 1) {
                temp = sectiontemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition2 = focusposition;
                return;
            }

            temp = sectiontemp.get(0);
            for(int j = 0 ; j < sectionaftertemp.size() ; j++) {
                if(temp.getContentDescription().equals(sectionaftertemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = 0;
                    listviewitemposition2 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("좋아요 표시한 동영상")) {
            ArrayList<TextView> liketemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeList;
            ArrayList<TextView> likeaftertemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeafterList;
            ListView listviewtemp = ((aLikeVideoActivity)aLikeVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(liketemp == null || likeaftertemp == null)
                return;

            else if(liketemp.size() == 1) {
                temp = liketemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition3 = focusposition;
                return;
            }

            temp = liketemp.get(0);
            for(int j = 0 ; j < likeaftertemp.size() ; j++) {
                if(temp.getContentDescription().equals(likeaftertemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = 0;
                    listviewitemposition3 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("공지사항")) {
            ArrayList<TextView> noticetemp = ((aNoticeActivity)aNoticeActivity.context).noticeList;
            ArrayList<TextView> noticeaftertemp = ((aNoticeActivity)aNoticeActivity.context).noticeafterList;
            ListView listviewtemp = ((aNoticeActivity)aNoticeActivity.context).listView;
            TextView temp;
            int focusposition;

            if(noticetemp == null || noticeaftertemp == null)
                return;

            else if(noticetemp.size() == 1) {
                temp = noticetemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition4 = focusposition;
                return;
            }

            temp = noticetemp.get(0);
            for(int j = 0 ; j < noticeaftertemp.size() ; j++) {
                if(temp.getContentDescription().equals(noticeaftertemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = 0;
                    listviewitemposition4 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("검색 목록")) {
            ArrayList<TextView> searchtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchList;
            ArrayList<TextView> searchaftertemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchafterList;
            ListView listviewtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(searchtemp == null || searchaftertemp == null)
                return;

            else if(searchtemp.size() == 1) {
                temp = searchtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition5 = focusposition;
                return;
            }

            temp = searchtemp.get(0);
            for(int j = 0 ; j < searchaftertemp.size() ; j++) {
                if(temp.getContentDescription().equals(searchaftertemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = 0;
                    listviewitemposition5 = focusposition;
                    return;
                }
            }
        }
    }

    public void listViewAutoFocusUp() {
        if(source.getContentDescription().equals("영상 목록")) {
            ArrayList<TextView> rowtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowList;
            ArrayList<TextView> rowbeforetemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowbeforeList;
            ListView listviewtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(rowtemp == null || rowbeforetemp == null)
                return;

            else if(rowtemp.size() == 1) {
                temp = rowtemp.get(rowtemp.size()-1);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition = focusposition;
                return;
            }

            temp = rowtemp.get(rowtemp.size()-1);
            for(int j = 0 ; j < rowbeforetemp.size() ; j++) {
                if(temp.getContentDescription().equals(rowbeforetemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = (rowtemp.size()-1);
                    listviewitemposition = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("파트 선택")) {
            ArrayList<TextView> sectiontemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionList;
            ArrayList<TextView> sectionbeforetemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionbeforeList;
            ListView listviewtemp = ((aSelectPartActivity)aSelectPartActivity.context).listView;
            TextView temp;
            int focusposition;

            if(sectiontemp == null || sectionbeforetemp == null)
                return;

            else if(sectiontemp.size() == 1) {
                temp = sectiontemp.get(sectiontemp.size()-1);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition2 = focusposition;
                return;
            }

            temp = sectiontemp.get(sectiontemp.size()-1);
            for(int j = 0 ; j < sectionbeforetemp.size() ; j++) {
                if(temp.getContentDescription().equals(sectionbeforetemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = (sectiontemp.size()-1);
                    listviewitemposition2 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("좋아요 표시한 동영상")) {
            ArrayList<TextView> liketemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeList;
            ArrayList<TextView> likebeforetemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likebeforeList;
            ListView listviewtemp = ((aLikeVideoActivity)aLikeVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(liketemp == null || likebeforetemp == null)
                return;

            else if(liketemp.size() == 1) {
                temp = liketemp.get(liketemp.size()-1);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition3 = focusposition;
                return;
            }

            temp = liketemp.get(liketemp.size()-1);
            for(int j = 0 ; j < likebeforetemp.size() ; j++) {
                if(temp.getContentDescription().equals(likebeforetemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = (liketemp.size()-1);
                    listviewitemposition3 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("공지사항")) {
            ArrayList<TextView> noticetemp = ((aNoticeActivity)aNoticeActivity.context).noticeList;
            ArrayList<TextView> noticebeforetemp = ((aNoticeActivity)aNoticeActivity.context).noticebeforeList;
            ListView listviewtemp = ((aNoticeActivity)aNoticeActivity.context).listView;
            TextView temp;
            int focusposition;

            if(noticetemp == null || noticebeforetemp == null)
                return;

            else if(noticetemp.size() == 1) {
                temp = noticetemp.get(noticetemp.size()-1);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition4 = focusposition;
                return;
            }

            temp = noticetemp.get(noticetemp.size()-1);
            for(int j = 0 ; j < noticebeforetemp.size() ; j++) {
                if(temp.getContentDescription().equals(noticebeforetemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = (noticetemp.size()-1);
                    listviewitemposition4 = focusposition;
                    return;
                }
            }
        }

        else if(source.getContentDescription().equals("검색 목록")) {
            ArrayList<TextView> searchtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchList;
            ArrayList<TextView> searchbeforetemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchbeforeList;
            ListView listviewtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).listView;
            TextView temp;
            int focusposition;

            if(searchtemp == null || searchbeforetemp == null)
                return;

            else if(searchtemp.size() == 1) {
                temp = searchtemp.get(searchtemp.size()-1);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition5 = focusposition;
                return;
            }

            temp = searchtemp.get(searchtemp.size()-1);
            for(int j = 0 ; j < searchbeforetemp.size() ; j++) {
                if(temp.getContentDescription().equals(searchbeforetemp.get(j).getContentDescription())) {
                    listviewtemp.findViewsWithText(arr, temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    arr2.get(0).setFocusable(true);
                    arr2.get(0).setFocusableInTouchMode(true);
                    arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                    arr2.remove(0);
                    arr.remove(0);
                    focusposition = (searchtemp.size()-1);
                    listviewitemposition5 = focusposition;
                    return;
                }
            }
        }
    }

    public void listViewGestureLeft() {
        ActivityManager am3 = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti3 = am3.getRunningTasks(1);
        if((rti3.get(0).topActivity.getClassName()).contains("aRecentVideoActivity")) {
            ArrayList<TextView> rowtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowList;
            ArrayList<TextView> rowaftertemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowafterList;
            ListView listviewtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).listView;
            TextView temp;
            TextView tempafter;
            int focusposition;

            if(rowtemp == null || rowaftertemp == null)
                return;

            else if(rowtemp.size() == 1) {
                temp = rowtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition = focusposition;
                return;
            }

            for(int i = 0 ; i < rowtemp.size() ; i++) {
                temp = rowtemp.get(i);
                temp.setFocusable(true);
                temp.setFocusableInTouchMode(true);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if (temp.getId() == rowtemp.size() - 1) {
                        tempafter = rowtemp.get(0);
                        for(int j = 0 ; j < rowaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((rowaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                Log.e(TAG, "arr2 : " + arr2.get(0));
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = 0;
                                listviewitemposition = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i + 1 == rowtemp.size())
                            break;
                        tempafter = rowtemp.get(i+1);
                        for(int j = 0 ; j < rowaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((rowaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i + 1;
                                listviewitemposition = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aSelectPartActivity")) {
            ArrayList<TextView> sectiontemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionList;
            ArrayList<TextView> sectionaftertemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionafterList;
            ListView listviewtemp = ((aSelectPartActivity)aSelectPartActivity.context).listView;
            TextView temp;
            TextView tempafter;
            int focusposition;

            if(sectiontemp == null || sectionaftertemp == null)
                return;

            else if(sectiontemp.size() == 1) {
                temp = sectiontemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition2 = focusposition;
                return;
            }

            for(int i = 0 ; i < sectiontemp.size() ; i++) {
                temp = sectiontemp.get(i);
                temp.setFocusable(true);
                temp.setFocusableInTouchMode(true);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if (temp.getId() == sectiontemp.size() - 1) {
                        tempafter = sectiontemp.get(0);
                        for(int j = 0 ; j < sectionaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((sectionaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                Log.e(TAG, "arr2 : " + arr2.get(0));
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = 0;
                                listviewitemposition2 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i + 1 == sectiontemp.size())
                            break;
                        tempafter = sectiontemp.get(i+1);
                        for(int j = 0 ; j < sectionaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((sectionaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i + 1;
                                listviewitemposition2 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aLikeVideoActivity")) {
            ArrayList<TextView> liketemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeList;
            ArrayList<TextView> likeaftertemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeafterList;
            ListView listviewtemp = ((aLikeVideoActivity)aLikeVideoActivity.context).listView;
            TextView temp;
            TextView tempafter;
            int focusposition;

            if(liketemp == null || likeaftertemp == null)
                return;

            else if(liketemp.size() == 1) {
                temp = liketemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition3 = focusposition;
                return;
            }

            for(int i = 0 ; i < liketemp.size() ; i++) {
                temp = liketemp.get(i);
                temp.setFocusable(true);
                temp.setFocusableInTouchMode(true);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if (temp.getId() == liketemp.size() - 1) {
                        tempafter = liketemp.get(0);
                        for(int j = 0 ; j < likeaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((likeaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                Log.e(TAG, "arr2 : " + arr2.get(0));
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = 0;
                                listviewitemposition3 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i + 1 == liketemp.size())
                            break;
                        tempafter = liketemp.get(i+1);
                        for(int j = 0 ; j < likeaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((likeaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i + 1;
                                listviewitemposition3 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aNoticeActivity")) {
            ArrayList<TextView> noticetemp = ((aNoticeActivity)aNoticeActivity.context).noticeList;
            ArrayList<TextView> noticeaftertemp = ((aNoticeActivity)aNoticeActivity.context).noticeafterList;
            ListView listviewtemp = ((aNoticeActivity)aNoticeActivity.context).listView;
            TextView temp;
            TextView tempafter;
            int focusposition;

            if(noticetemp == null || noticeaftertemp == null)
                return;

            else if(noticetemp.size() == 1) {
                temp = noticetemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition4 = focusposition;
                return;
            }

            for(int i = 0 ; i < noticetemp.size() ; i++) {
                temp = noticetemp.get(i);
                temp.setFocusable(true);
                temp.setFocusableInTouchMode(true);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if (temp.getId() == noticetemp.size() - 1) {
                        tempafter = noticetemp.get(0);
                        for(int j = 0 ; j < noticeaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((noticeaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                Log.e(TAG, "arr2 : " + arr2.get(0));
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = 0;
                                listviewitemposition4 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i + 1 == noticetemp.size())
                            break;
                        tempafter = noticetemp.get(i+1);
                        for(int j = 0 ; j < noticeaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((noticeaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i + 1;
                                listviewitemposition4 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aSearchVideoActivity")) {
            ArrayList<TextView> searchtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchList;
            ArrayList<TextView> searchaftertemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchafterList;
            ListView listviewtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).listView;
            TextView temp;
            TextView tempafter;
            int focusposition;

            if(searchtemp == null || searchaftertemp == null)
                return;

            else if(searchtemp.size() == 1) {
                temp = searchtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition5 = focusposition;
                return;
            }

            for(int i = 0 ; i < searchtemp.size() ; i++) {
                temp = searchtemp.get(i);
                temp.setFocusable(true);
                temp.setFocusableInTouchMode(true);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if (temp.getId() == searchtemp.size() - 1) {
                        tempafter = searchtemp.get(0);
                        for(int j = 0 ; j < searchaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((searchaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                Log.e(TAG, "arr2 : " + arr2.get(0));
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = 0;
                                listviewitemposition5 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i + 1 == searchtemp.size())
                            break;
                        tempafter = searchtemp.get(i+1);
                        for(int j = 0 ; j < searchaftertemp.size() ; j++) {
                            if((tempafter.getContentDescription()).equals((searchaftertemp.get(j)).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempafter.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i + 1;
                                listviewitemposition5 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void listViewGestureRight() {
        ActivityManager am3 = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti3 = am3.getRunningTasks(1);

        if((rti3.get(0).topActivity.getClassName()).contains("aRecentVideoActivity")) {
            ArrayList<TextView> rowtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowList;
            ArrayList<TextView> rowbeforetemp = ((aRecentVideoActivity)aRecentVideoActivity.context).rowbeforeList;
            ListView listviewtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).listView;
            TextView temp;
            TextView tempbefore;
            int focusposition;

            if(rowtemp == null || rowbeforetemp == null)
                return;

            else if(rowtemp.size() == 1) {
                temp = rowtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition = focusposition;
                return;
            }

            for(int i = 0 ; i < rowtemp.size() ; i++) {
                temp = rowtemp.get(i);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if(temp.getId() == 0) {
                        tempbefore = rowtemp.get(rowtemp.size() - 1);
                        for(int j = 0 ; j < rowbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(rowbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = (rowtemp.size() - 1);
                                listviewitemposition = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i -1 < 0)
                            break;
                        tempbefore = rowtemp.get(i - 1);
                        for(int j = 0 ; j < rowbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(rowbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i - 1;
                                listviewitemposition = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aSelectPartActivity")) {
            ArrayList<TextView> sectiontemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionList;
            ArrayList<TextView> sectionbeforetemp = ((aSelectPartActivity)aSelectPartActivity.context).sectionbeforeList;
            ListView listviewtemp = ((aSelectPartActivity)aSelectPartActivity.context).listView;
            TextView temp;
            TextView tempbefore;
            int focusposition;

            if(sectiontemp == null || sectionbeforetemp == null)
                return;

            else if(sectiontemp.size() == 1) {
                temp = sectiontemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition2 = focusposition;
                return;
            }

            for(int i = 0 ; i < sectiontemp.size() ; i++) {
                temp = sectiontemp.get(i);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if(temp.getId() == 0) {
                        tempbefore = sectiontemp.get(sectiontemp.size() - 1);
                        for(int j = 0 ; j < sectionbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(sectionbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = (sectiontemp.size() - 1);
                                listviewitemposition2 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i -1 < 0)
                            break;
                        tempbefore = sectiontemp.get(i - 1);
                        for(int j = 0 ; j < sectionbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(sectionbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i - 1;
                                listviewitemposition2 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aLikeVideoActivity")) {
            ArrayList<TextView> liketemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likeList;
            ArrayList<TextView> likebeforetemp = ((aLikeVideoActivity)aLikeVideoActivity.context).likebeforeList;
            ListView listviewtemp = ((aLikeVideoActivity)aLikeVideoActivity.context).listView;
            TextView temp;
            TextView tempbefore;
            int focusposition;

            if(liketemp == null || likebeforetemp == null)
                return;

            else if(liketemp.size() == 1) {
                temp = liketemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition3 = focusposition;
                return;
            }

            for(int i = 0 ; i < liketemp.size() ; i++) {
                temp = liketemp.get(i);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if(temp.getId() == 0) {
                        tempbefore = liketemp.get(liketemp.size() - 1);
                        for(int j = 0 ; j < likebeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(likebeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = (liketemp.size() - 1);
                                listviewitemposition3 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i -1 < 0)
                            break;
                        tempbefore = liketemp.get(i - 1);
                        for(int j = 0 ; j < likebeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(likebeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i - 1;
                                listviewitemposition3 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aNoticeActivity")) {
            ArrayList<TextView> noticetemp = ((aNoticeActivity)aNoticeActivity.context).noticeList;
            ArrayList<TextView> noticebeforetemp = ((aNoticeActivity)aNoticeActivity.context).noticebeforeList;
            ListView listviewtemp = ((aNoticeActivity)aNoticeActivity.context).listView;
            TextView temp;
            TextView tempbefore;
            int focusposition;

            if(noticetemp == null || noticebeforetemp == null)
                return;

            else if(noticetemp.size() == 1) {
                temp = noticetemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition4 = focusposition;
                return;
            }

            for(int i = 0 ; i < noticetemp.size() ; i++) {
                temp = noticetemp.get(i);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if(temp.getId() == 0) {
                        tempbefore = noticetemp.get(noticetemp.size() - 1);
                        for(int j = 0 ; j < noticebeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(noticebeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = (noticetemp.size() - 1);
                                listviewitemposition4 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i -1 < 0)
                            break;
                        tempbefore = noticetemp.get(i - 1);
                        for(int j = 0 ; j < noticebeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(noticebeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i - 1;
                                listviewitemposition4 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }

        else if((rti3.get(0).topActivity.getClassName()).contains("aSearchVideoActivity")) {
            ArrayList<TextView> searchtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchList;
            ArrayList<TextView> searchbeforetemp = ((aSearchVideoActivity)aSearchVideoActivity.context).searchbeforeList;
            ListView listviewtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).listView;
            TextView temp;
            TextView tempbefore;
            int focusposition;

            if(searchtemp == null || searchbeforetemp == null)
                return;

            else if(searchtemp.size() == 1) {
                temp = searchtemp.get(0);
                listviewtemp.findViewsWithText(arr2, "constraint_" + temp.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                arr2.get(0).setFocusable(true);
                arr2.get(0).setFocusableInTouchMode(true);
                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                arr2.remove(0);
                focusposition = 0;
                listviewitemposition5 = focusposition;
                return;
            }

            for(int i = 0 ; i < searchtemp.size() ; i++) {
                temp = searchtemp.get(i);
                if ((source.getContentDescription()).equals("constraint_"+temp.getContentDescription())) {
                    if(temp.getId() == 0) {
                        tempbefore = searchtemp.get(searchtemp.size() - 1);
                        for(int j = 0 ; j < searchbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(searchbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = (searchtemp.size() - 1);
                                listviewitemposition5 = focusposition;
                                return;
                            }
                        }
                    }

                    else {
                        if(i -1 < 0)
                            break;
                        tempbefore = searchtemp.get(i - 1);
                        for(int j = 0 ; j < searchbeforetemp.size() ; j++) {
                            if((tempbefore.getContentDescription()).equals(searchbeforetemp.get(j).getContentDescription())) {
                                listviewtemp.findViewsWithText(arr, tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                listviewtemp.findViewsWithText(arr2, "constraint_" + tempbefore.getContentDescription(), ViewStub.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                                arr2.get(0).setFocusable(true);
                                arr2.get(0).setFocusableInTouchMode(true);
                                arr2.get(0).performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
                                arr2.remove(0);
                                arr.remove(0);
                                focusposition = i - 1;
                                listviewitemposition5 = focusposition;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onGesture(int gestureId) {
        if(source == null)
            return false;

        switch(gestureId) {
            //GESTURE_SWIPE_LEFT = 3
            //GESTURE_SWIPE_RIGHT = 4
            //GESTURE_SWIPE_UP = 1
            //GESTURE_SWIPE_DOWN = 2
            // 오른쪽에서 왼쪽으로 스와이프를 진행하는 경우, 해당 레이아웃 내에서 선택되어 있던 UI 컴포넌트의 다음 컴포넌트를 포커싱 및 선택
            case GESTURE_SWIPE_LEFT:
                Toast.makeText(getApplication(), "SWIPE_LEFT", Toast.LENGTH_LONG).show();
                if(source.getViewIdResourceName() != null) {
                    if((source.getContentDescription().equals("영상 목록") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("파트 선택") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("좋아요 표시한 동영상") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("공지사항") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("검색 목록") && source.getViewIdResourceName().contains("textView9"))) {
                        listViewAutoFocusDown();
                        return true;
                    }

                    if(source.getViewIdResourceName().contains("constraint1")){
                        listViewGestureLeft();
                        return true;
                    }

                    temp = source.getTraversalAfter();

                    if(temp == null)
                        return false;
                    temp.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
                }

                else {
                    source = findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
                }
                return true;

            // 왼쪽에서 오른쪽으로 스와이프를 진행하는 경우, 해당 레이아웃 내에서 선택되어 있던 UI 컴포넌트의 이전 컴포넌트를 포커싱 및 선택
            case GESTURE_SWIPE_RIGHT:
                Toast.makeText(getApplication(), "SWIPE_RIGHT", Toast.LENGTH_LONG).show();
                if(source.getViewIdResourceName() != null) {
                    if((source.getContentDescription().equals("영상 목록") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("파트 선택") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("좋아요 표시한 동영상") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("공지사항") && source.getViewIdResourceName().contains("textView9")) || (source.getContentDescription().equals("검색 목록") && source.getViewIdResourceName().contains("textView9"))) {
                        listViewAutoFocusUp();
                        return true;
                    }
                    if(source.getViewIdResourceName().contains("constraint1")){
                        listViewGestureRight();
                        return true;
                    }

                    temp = source.getTraversalBefore();
                    if(temp == null)
                        return false;
                    temp.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
                }

                else {
                    source = findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
                }
                return true;

            // 아래쪽에서 위쪽으로 스와이프를 진행하는 경우, 해당 레이아웃 UI에서 이전 레이아웃 UI로 이동
            case GESTURE_SWIPE_UP:
                Toast.makeText(getApplication(), "SWIPE_UP", Toast.LENGTH_LONG).show();
                ActivityManager am1 = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> rti1 = am1.getRunningTasks(1);
                if((rti1.get(0).topActivity.getClassName()).contains("aLfreeMainActivity")) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    tts.speak("애플리케이션 종료하기", TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
                    disableSelf();
                    System.runFinalizersOnExit(true);
                    System.exit(0);
                }
                else {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    tts.speak("뒤로 가기", TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
                }
                return true;

            // 위쪽에서 아래쪽으로 스와이프를 진행하는 경우, 해당 레이아웃 UI에서 홈으로 이동
            case GESTURE_SWIPE_DOWN:
                Toast.makeText(getApplication(), "SWIPE_DOWN", Toast.LENGTH_LONG).show();
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                tts.speak("애플리케이션 종료하기", TextToSpeech.QUEUE_FLUSH, null, "TextToSpeech_ID");
                disableSelf();
                System.runFinalizersOnExit(true);
                System.exit(0);
                return true;
            default:
                Toast.makeText(getApplication(), "SWIPE_ETC", Toast.LENGTH_LONG).show();
                return false;
        }
    }

    // 이벤트가 발생할때마다 실행되는 부분
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 발생한 이벤트로부터 Source를 get
        source = event.getSource();
        // 실현 시간 상수로서 접근성 서비스에 대한 이벤트 타입 변수 선언 및 생성
        final int eventType =  event.getEventType();

        // 특정 이벤트에 대한 서술 변수
        String eventText = null;
        String introText = " \n실행하려면 두번 탭 하세요.";

        // 접근성 이벤트의 소스가 null이라면 예외가 발생한 것이므로 Side Effect가 발생하기 전에 바로 return
        if(source == null) {
            return;
        }

        if(source.getViewIdResourceName() == null || source.getContentDescription() == null) {
            //Toast.makeText(getApplicationContext(), "VIEW ID or Content is NULL!", Toast.LENGTH_LONG).show();
            return;
        }

        ActivityManager am2 = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti2 = am2.getRunningTasks(1);
        if(!((rti2.get(0).topActivity.getClassName()).contains("aRecentVideoActivity")))
            timercount = 0;
        else if(!((rti2.get(0).topActivity.getClassName()).contains("aSelectPartActivity")))
            timercount2 = 0;
        else if(!((rti2.get(0).topActivity.getClassName()).contains("aLikeVideoActivity")))
            timercount3 = 0;
        else if(!((rti2.get(0).topActivity.getClassName()).contains("aNoticeActivity")))
            timercount4 = 0;
        else if(!((rti2.get(0).topActivity.getClassName()).contains("aSearchVideoActivity")))
            timercount5 = 0;

        if(eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
            // 이벤트를 발생시킨 해당 소스에 대한 Action 실행. 이 때의 Action은 접근성 서비스를 위한 FOCUS
            eventText = "클릭됨 : ";
            eventText = eventText + event.getContentDescription();
            eventText = eventText.replace("constraint_", "");
            Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
            source.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
            // 다시 사용할 수 있도록 해당 인스턴스를 반환
            source.recycle();
        }

        else{
            switch (eventType) {
                // 특정 컴포넌트에 대해 접근성 관련 포커싱이 발생하면
                case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                    eventText = "접근성 포커싱됨 : ";
                    break;
                // AdapterView에 적재되어 있는 특정 갯수의 아이템이 선택될 시
                case AccessibilityEvent.TYPE_VIEW_SELECTED:
                    eventText = "선택됨 : ";
                    break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                    eventText = "포커싱됨 : ";
                    break;
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                    eventText = "스크롤 중 : ";
                    break;
            }
            // 이벤트의 대상이 된 컴포넌트의 ContentDescription 내용을 String에 저장 및 출력
            if(eventText != null) {
                eventText = eventText + event.getContentDescription();

                ActivityManager am3 = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> rti3 = am3.getRunningTasks(1);

                if(eventText.length() < 30 && eventType != AccessibilityEvent.TYPE_VIEW_SELECTED && eventType != AccessibilityEvent.TYPE_VIEW_FOCUSED && eventType != AccessibilityEvent.TYPE_VIEW_SCROLLED && eventType != AccessibilityEvent.TYPE_VIEW_CLICKED && !source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/textView9") && !source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/lfree") && (!(rti3.get(0).topActivity.getClassName()).contains("aRecentVideoActivity")) && (!(rti3.get(0).topActivity.getClassName()).contains("aSelectPartActivity")) && (!(rti3.get(0).topActivity.getClassName()).contains("aLikeVideoActivity")) && (!(rti3.get(0).topActivity.getClassName()).contains("aNoticeActivity")) && (!(source.getContentDescription().equals("버전 정보 1.0.0.0"))))
                    eventText = eventText + introText;
                    //else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/title"))
                else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/constraint1") && (rti3.get(0).topActivity.getClassName()).contains("aRecentVideoActivity")) {
                    eventText = eventText.replace("constraint_", "");
                    //eventText = eventText + introText;
                    eventText = eventText + " 제스처를 수행하지 않을 시, 15초 이후로 실행됩니다.";
                }
                else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/constraint1") && (rti3.get(0).topActivity.getClassName()).contains("aSelectPartActivity")) {
                    eventText = eventText.replace("constraint_", "");
                    eventText = eventText.replace("접근성 포커싱됨 : ", "");
                    eventText = "접근성 포커싱됨 : 음성 해설 파트 " + eventText + "\n\n" + "제스처를 수행하지 않을 시, 8초 이후로 선택합니다.";
                }
                else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/constraint1") && (rti3.get(0).topActivity.getClassName()).contains("aLikeVideoActivity")) {
                    eventText = eventText.replace("constraint_", "");
                    eventText = eventText + " 제스처를 수행하지 않을 시, 12초 이후로 실행됩니다.";
                }
                else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/constraint1") && (rti3.get(0).topActivity.getClassName()).contains("aNoticeActivity"))
                    eventText = eventText.replace("constraint_", "");
                else if(source.getViewIdResourceName().equalsIgnoreCase("com.saenaegi.lfree:id/constraint1") && (rti3.get(0).topActivity.getClassName()).contains("aSearchVideoActivity")) {
                    eventText = eventText.replace("constraint_", "");
                    eventText = eventText + " 제스처를 수행하지 않을 시, 13초 이후로 실행됩니다.";
                }

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

                if(eventText.contains("15초 이후로")) {
                    if(timercount != 0 && mHandler != null){
                        mHandler.removeMessages(0);
                        mHandler = null;
                    }
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            ListView listviewtemp = ((aRecentVideoActivity)aRecentVideoActivity.context).listView;
                            listviewtemp.performItemClick(listviewtemp.getAdapter().getView(listviewitemposition, null, null), listviewitemposition, listviewtemp.getItemIdAtPosition(listviewitemposition));
                        }
                    }, 15000);
                    timercount++;
                }

                else if(eventText.contains("8초 이후로")) {
                    if(timercount2 != 0 && mHandler2 != null){
                        mHandler2.removeMessages(0);
                        mHandler2 = null;
                    }
                    mHandler2 = new Handler();
                    mHandler2.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            ListView listviewtemp = ((aSelectPartActivity)aSelectPartActivity.context).listView;
                            listviewtemp.performItemClick(listviewtemp.getAdapter().getView(listviewitemposition2, null, null), listviewitemposition2, listviewtemp.getItemIdAtPosition(listviewitemposition2));
                        }
                    }, 8000);

                    timercount2++;
                }

                else if(eventText.contains("12초 이후로")) {
                    if(timercount3 != 0 && mHandler3 != null){
                        mHandler3.removeMessages(0);
                        mHandler3 = null;
                    }
                    mHandler3 = new Handler();
                    mHandler3.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            ListView listviewtemp = ((aLikeVideoActivity)aLikeVideoActivity.context).listView;
                            listviewtemp.performItemClick(listviewtemp.getAdapter().getView(listviewitemposition3, null, null), listviewitemposition3, listviewtemp.getItemIdAtPosition(listviewitemposition3));
                        }
                    }, 12000);

                    timercount3++;
                }

                else if(eventText.contains("13초 이후로")) {
                    if(timercount5 != 0 && mHandler5 != null){
                        mHandler5.removeMessages(0);
                        mHandler5 = null;
                    }
                    mHandler5 = new Handler();
                    mHandler5.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            ListView listviewtemp = ((aSearchVideoActivity)aSearchVideoActivity.context).listView;
                            listviewtemp.performItemClick(listviewtemp.getAdapter().getView(listviewitemposition5, null, null), listviewitemposition5, listviewtemp.getItemIdAtPosition(listviewitemposition5));
                        }
                    }, 13000);

                    timercount5++;
                }
            }

            else
                return;
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
                | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END
                | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
                | AccessibilityEvent.TYPE_VIEW_SELECTED | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER;

        // 이벤트 발생 시 음성 및 진동 피드백 제공
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN | AccessibilityServiceInfo.FEEDBACK_HAPTIC;

        // 제스처 기능 수행을 위한 터치 모드 수행 플래그 설정
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;

        info.notificationTimeout = 100; // millisecond
        // 서비스 설정
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        // 특정 컴포넌트에 대한 tts 기능 사용 후에는 반드시 shutdown을 시켜 리소스 낭비를 피하도록 한다.
        // 이 때 TTS 기능이 shutdown이 되더라도 다시 enable하면 되므로 상관없다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}