package com.saenaegi.lfree;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;


public class MainActivity extends AppCompatActivity {

    private ImageView mainLogo;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLogo = (ImageView)findViewById(R.id.main_logo);
        Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_move);
        mainLogo.startAnimation(logoMoveAnimation);

        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        signInButton.startAnimation(fadeInAnimation);

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}