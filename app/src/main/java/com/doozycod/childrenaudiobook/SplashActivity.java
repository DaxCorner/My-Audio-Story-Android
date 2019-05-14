package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    int[] images = {R.drawable.a01,
            R.drawable.a02, R.drawable.a03, R.drawable.a04, R.drawable.a05, R.drawable.a06, R.drawable.a07, R.drawable.a08,
            R.drawable.a09, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
            R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22};
    ImageView loading_bar;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Permissions.Check_RECORD_AUDIO(SplashActivity.this) && !Permissions.Check_STORAGE(SplashActivity.this)) {

            Permissions.Request_STORAGE(SplashActivity.this, 12);
            Permissions.Request_RECORD_AUDIO(SplashActivity.this, 22);
        }

        setContentView(R.layout.activity_splash);
        loading_bar = findViewById(R.id.loading);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (i < 22) {
                    loading_bar.setImageResource(images[i]);
                    i++;
                }
                handler.postDelayed(this, 100);

            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2 * 1400);

    }
}
