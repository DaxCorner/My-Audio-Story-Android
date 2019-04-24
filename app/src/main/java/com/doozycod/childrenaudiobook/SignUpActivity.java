package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    Button sign_up_user;
    TextView goto_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up_user = findViewById(R.id.sign_upactivity_btn);
        goto_signin = findViewById(R.id.sign_ingoto);
        goto_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, Login.class));
                finish();
            }
        });
        sign_up_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this, RecordedAudioActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}
