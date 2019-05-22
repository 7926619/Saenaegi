package com.saenaegi.lfree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class aSetPushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_set_push);

        TextView tv1 = (TextView) findViewById(R.id.textView18);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) { }
        });
        TextView tv2 = (TextView) findViewById(R.id.textView17);
        tv2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) { }
        });
        TextView tv3 = (TextView) findViewById(R.id.back);
        tv3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent(aSetPushActivity.this, aSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
