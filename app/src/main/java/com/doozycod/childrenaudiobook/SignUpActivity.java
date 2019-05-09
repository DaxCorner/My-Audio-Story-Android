package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class SignUpActivity extends AppCompatActivity {
    Button sign_up_user;
    ImageView login_button, library_buton, home_button;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myDialog = new Dialog(this);
        sign_up_user = findViewById(R.id.sign_upactivity_btn);
        home_button = findViewById(R.id.home_btn);
        library_buton = findViewById(R.id.lib_btn);
        login_button = findViewById(R.id.login_btn);
        sign_up_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, RecordedAudioActivity.class));
                finish();
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        library_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));

            }
        });
    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.custom_login_popup);

        Button login_dialog;
//        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);
//
//        login_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }


}
