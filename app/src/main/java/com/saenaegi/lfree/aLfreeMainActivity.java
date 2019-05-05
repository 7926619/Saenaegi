package com.saenaegi.lfree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;

public class aLfreeMainActivity extends AppCompatActivity {
    List<AccessibilityServiceInfo> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_lfree_main);

        // 접근성 권한이 없으면 접근성 권한 설정하는 다이얼로그 띄워주는 부분
        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }
    }

    // 접근성 권한이 있는지 없는지 확인하는 부분
    // 있으면 true, 없으면 false
    public boolean checkAccessibilityPermissions() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        if(accessibilityManager == null) {
            Toast.makeText(getApplicationContext(), "Accessibility Service Manager is NULL!", Toast.LENGTH_LONG).show();
            return false;
        }

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        if(list.size() == 0)
            Toast.makeText(getApplicationContext(), "Accessibility Service List is NULL!", Toast.LENGTH_LONG).show();

        for (int i = 0, count = list.size() ; i < count ; ++i) {
            AccessibilityServiceInfo info = list.get(i);
            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // 접근성 설정화면으로 넘겨주는 부분
    public void setAccessibilityPermissions() {
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("제스처 권한 설정");
        gsDialog.setMessage("제스처 기능을 사용하기 위해서는 접근성 권한을 필요로 합니다");
        gsDialog.setPositiveButton("동의", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
                //return;
            }
        }).create().show();
    }

    // 접근성 서비스 on으로 설정 후, 다시 앱으로 돌아왔을 때 동작처리
    // 물론 접근성 서비스를 on하지 않고 그대로 다시 앱으로 돌아왔을 때는 다시 접근성 서비스 설정 화면으로 이동
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                if(list.contains(getApplication().getPackageName()))
                    Toast.makeText(getApplicationContext(), "Accessibility Service : Connected", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "Accessibility Service : Not Connected", Toast.LENGTH_SHORT).show();
                    setAccessibilityPermissions();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
