package com.saenaegi.lfree;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class aLfreeMainActivity extends AppCompatActivity {
    AccessibilityManager accessibilityManager;
    List<AccessibilityServiceInfo> list;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_lfree_main);

        TextView tv1 = (TextView) findViewById(R.id.textView18);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aHowToUseActivity.class);
                startActivity(intent);
            }
        });
        TextView tv2 = (TextView) findViewById(R.id.textView17);
        tv2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aWatchVideoMenuActivitiy.class);
                startActivity(intent);
            }
        });
        TextView tv3 = (TextView) findViewById(R.id.textView16);
        tv3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aRequestVideoActivity.class);
                startActivity(intent);
            }
        });
        TextView tv4 = (TextView) findViewById(R.id.textView15);
        tv4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aLikeVideoActivity.class);
                startActivity(intent);
            }
        });
        TextView tv5 = (TextView) findViewById(R.id.textView14);
        tv5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aNoticeActivity.class);
                startActivity(intent);
            }
        });
        TextView tv6 = (TextView) findViewById(R.id.textView13);
        tv6.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aLfreeMainActivity.this, aSettingsActivity.class);
                startActivity(intent);
            }
        });
        TextView tv7 = (TextView) findViewById(R.id.textView12);
        tv7.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                finish();
            }
        });

        // 접근성 권한이 없으면 접근성 권한 설정하는 다이얼로그 띄워주는 부분
        check();
    }

    public void check() {
        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }
    }

    // 접근성 권한이 있는지 없는지 확인하는 부분
    // 있으면 true, 없으면 false
    public boolean checkAccessibilityPermissions() {
        accessibilityManager = (AccessibilityManager) this.getSystemService(Context.ACCESSIBILITY_SERVICE);

        if(accessibilityManager == null) {
            Toast.makeText(getApplicationContext(), "Accessibility Service Manager is NULL!", Toast.LENGTH_LONG).show();
            return false;
        }
        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        if(list.size() == 0) {
            Toast.makeText(getApplicationContext(), "Accessibility Service List is NULL!", Toast.LENGTH_LONG).show();
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                Toast.makeText(getApplicationContext(), "Now LFREE is exist in Accessibility Service List", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        Toast.makeText(getApplicationContext(), "LFREE is not exist in Accessibility Service List", Toast.LENGTH_LONG).show();
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
                return;
            }
        }).create().show();
    }

    // 접근성 서비스 on으로 설정 후, 다시 앱으로 돌아왔을 때 동작처리
    // 물론 접근성 서비스를 on하지 않고 그대로 다시 앱으로 돌아왔을 때는 다시 접근성 서비스 설정 화면으로 이동
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                for (int i = 0; i < list.size(); i++) {
                    AccessibilityServiceInfo info = list.get(i);

                    // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
                    if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                        Toast.makeText(getApplicationContext(), "Accessibility Service : Connected", Toast.LENGTH_LONG).show();
                        count = 1;
                    }
                }
                if(count == 0){
                    Toast.makeText(getApplicationContext(), "Accessibility Service : Not Connected", Toast.LENGTH_LONG).show();
                    check();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}