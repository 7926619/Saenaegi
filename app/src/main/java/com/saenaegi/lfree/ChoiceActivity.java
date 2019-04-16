package com.saenaegi.lfree;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChoiceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        ImageButton noButton = (ImageButton)findViewById(R.id.no);
        ImageButton yesButton = (ImageButton)findViewById(R.id.yes);

        noButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent=new Intent(ChoiceActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
