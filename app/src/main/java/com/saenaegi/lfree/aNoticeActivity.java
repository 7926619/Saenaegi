package com.saenaegi.lfree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class aNoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_notice);
    }
}