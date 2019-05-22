package com.doozycod.childrenaudiobook.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

public class SplashActivity extends AppCompatActivity {
    int[] images = {R.drawable.a01,
            R.drawable.a02, R.drawable.a03, R.drawable.a04, R.drawable.a05, R.drawable.a06, R.drawable.a07, R.drawable.a08,
            R.drawable.a09, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
            R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22};
    ImageView loading_bar;
    int i = 0;
    SharedPreferenceMethod sharedPreferenceMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        loading_bar = findViewById(R.id.loading);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);

        sharedPreferenceMethod.saveLogin(false);

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
                startActivity(new Intent(SplashActivity.this, ChooseYourBookActivity.class));
                finish();
            }
        }, 2 * 1400);

    }

}
