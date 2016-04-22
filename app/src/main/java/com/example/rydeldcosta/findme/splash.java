package com.example.rydeldcosta.findme;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.splash_image), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.splash_image_1), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.splash_image_2), 900);
        animation.setOneShot(true);

        ImageView logoimg = (ImageView) findViewById(R.id.logoimg);
        logoimg.setImageDrawable(animation);

        //    animation = (AnimationDrawable) logoimg.getBackground();
        animation.start();


        String fontpath = "Fonts/Jellyka_Estrya_Handwriting.ttf";
        TextView logoname = (TextView) findViewById(R.id.logoname);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontpath);
        logoname.setTypeface(tf);

        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        logoname.startAnimation(animationFadeIn);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(splash.this, login.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() { }
}
