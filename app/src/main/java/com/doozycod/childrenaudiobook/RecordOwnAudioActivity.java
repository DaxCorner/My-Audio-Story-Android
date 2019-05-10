package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class RecordOwnAudioActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    int i = 0;
    ImageView record_btn;
    ImageView imageView;
    RadioButton regular_button, large_button;
    Dialog myDialog;
    int[] count_down_timer_img = {R.drawable.countdown_02, R.drawable.countdown_01, R.drawable.countdown_00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDialog = new Dialog(this);
        setContentView(R.layout.activity_record_own);
        record_btn = findViewById(R.id.recording_start);
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });


    }

    public void ShowPopup(final View v) {
        myDialog.setContentView(R.layout.custom_record_timer);
         imageView = myDialog.findViewById(R.id.counter_image);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (i < 3) {
                    imageView.setImageResource(count_down_timer_img[i]);
                    i++;
                }
                handler.postDelayed(this, 1000);

            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(RecordOwnAudioActivity.this, StartRecordingActivity.class));
                finish();
                myDialog.dismiss();
            }
        }, 3 * 1100);
        myDialog.show();
    }


}
