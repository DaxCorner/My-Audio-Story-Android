package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class StartRecordingActivity extends AppCompatActivity {
    ImageView start_recording, imageView;
    Dialog myDialog;
    int i = 0;
    int[] count_down_timer_img = { R.drawable.countdown_29, R.drawable.countdown_28,R.drawable.countdown_27, R.drawable.countdown_26, R.drawable.countdown_25
            , R.drawable.countdown_24, R.drawable.countdown_23, R.drawable.countdown_22, R.drawable.countdown_21, R.drawable.countdown_20, R.drawable.countdown_19,
            R.drawable.countdown_18, R.drawable.countdown_17, R.drawable.countdown_16, R.drawable.countdown_15, R.drawable.countdown_14,
            R.drawable.countdown_13, R.drawable.countdown_12, R.drawable.countdown_11, R.drawable.countdown_10, R.drawable.countdown_09, R.drawable.countdown_08,
            R.drawable.countdown_07, R.drawable.countdown_06, R.drawable.countdown_05, R.drawable.countdown_04, R.drawable.countdown_03, R.drawable.countdown_02,
            R.drawable.countdown_01, R.drawable.countdown_00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_recording);
        myDialog = new Dialog(this);
        start_recording = findViewById(R.id.record_personal_msg);
        start_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.custom_personal_greet_popup);
        imageView = myDialog.findViewById(R.id.counter_image_personal);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (i < 30) {
                    imageView.setImageResource(count_down_timer_img[i]);
                    i++;
                }
                handler.postDelayed(this, 1000);

            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartRecordingActivity.this, StartRecordingActivity.class));
                finish();
                myDialog.dismiss();
            }
        }, 30 * 1050);
        myDialog.show();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }

}
