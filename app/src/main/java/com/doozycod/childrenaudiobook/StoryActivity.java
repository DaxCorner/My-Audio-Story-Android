package com.doozycod.childrenaudiobook;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StoryActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    Button record_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        record_btn = findViewById(R.id.record_audio);
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
    }

    private void popup() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Recording Starts in");
        alertDialog.setMessage("00:3");
        alertDialog.show();   //

        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("00:" + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
            }
        }.start();
    }

}
