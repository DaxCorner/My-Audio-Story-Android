package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ListenAudioStory extends AppCompatActivity {
    ImageView recordAudioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio_story);

        recordAudioButton = findViewById(R.id.record_audio_book);
        recordAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListenAudioStory.this, StoryActivity.class));
            }
        });
    }
}
