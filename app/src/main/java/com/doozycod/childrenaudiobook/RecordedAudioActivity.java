package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class RecordedAudioActivity extends AppCompatActivity {

    ImageView save_btn,share_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_audio);
        save_btn = findViewById(R.id.save_story_btn);
        share_btn = findViewById(R.id.share_story_btn_on_record);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordedAudioActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordedAudioActivity.this, "Shared!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
