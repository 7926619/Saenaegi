package com.saenaegi.lfree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class aOpenSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_open_source);

        TextView tv1 = (TextView) findViewById(R.id.textView18);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) { }
        });
    }
}
