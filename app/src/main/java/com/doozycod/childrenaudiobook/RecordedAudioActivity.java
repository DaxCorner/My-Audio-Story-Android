package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RecordedAudioActivity extends AppCompatActivity {
    Button share_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_audio);
        share_audio = findViewById(R.id.share_audio_btn);
        share_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordedAudioActivity.this, ShareStory.class));
            }
        });
    }
}
