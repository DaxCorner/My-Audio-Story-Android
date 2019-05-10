package com.doozycod.childrenaudiobook;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShareStory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_story);
        TextView tx = findViewById(R.id.email);
        TextView phone_no = findViewById(R.id.phone_no);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        tx.setTypeface(custom_font);
        phone_no.setTypeface(custom_font);

    }
}
