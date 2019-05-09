package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

public class RecordOwnAudioActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    ImageView record_btn;
    RadioButton regular_button, large_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_own);




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
                startActivity(new Intent(RecordOwnAudioActivity.this, RecordedAudioActivity.class));
            }
        }.start();
    }

}
