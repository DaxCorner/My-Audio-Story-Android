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

public class StoryActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    ImageView record_btn;
    RadioButton regular_button, large_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story);
        record_btn = findViewById(R.id.record_audio);
        regular_button = findViewById(R.id.firstRadio);
        large_button = findViewById(R.id.secondRadio);

        final LabeledSwitch labeledSwitch = findViewById(R.id.toggle_bttn);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    labeledSwitch.setLabelOn("OFF");
                }else{
                    labeledSwitch.setLabelOn("ON");
                }

            }
        });


        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });


        if (regular_button.isChecked()) {
            Toast.makeText(this, "Regular text Size", Toast.LENGTH_SHORT).show();

        } else if (large_button.isChecked()) {
            Toast.makeText(this, "Large text Size", Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(this, "By default text is in Regular size", Toast.LENGTH_SHORT).show();
            return;

        }


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
                startActivity(new Intent(StoryActivity.this, RecordedAudioActivity.class));
            }
        }.start();
    }

}
