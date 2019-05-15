package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.R;

import static com.doozycod.childrenaudiobook.R.drawable.bg_music_off_btn;
import static com.doozycod.childrenaudiobook.R.drawable.bg_music_on_btn;
import static com.doozycod.childrenaudiobook.R.drawable.large_font_btn;
import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;
import static com.doozycod.childrenaudiobook.R.drawable.small_font_btn;

public class RecordYourOwnActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    int i = 0;
    ImageView record_btn, large_font;
    ImageView imageView, bg_music_btn, home_btn_record, lib_btn, login_btn, login_dialog, popup_login, popup_signup;
    Dialog myDialog;
    int[] count_down_timer_img = {R.drawable.countdown_02, R.drawable.countdown_01, R.drawable.countdown_00};
    boolean isPressed = true;
    final Handler handler = new Handler();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDialog = new Dialog(this);
        setContentView(R.layout.activity_record_own);



        record_btn = findViewById(R.id.recording_start);
        large_font = findViewById(R.id.large_font);
        lib_btn = findViewById(R.id.lib_btn_record_own);
        login_btn = findViewById(R.id.login_btn_record_your_own);
        home_btn_record = findViewById(R.id.home_btn_record_own);

        large_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPressed) {
//                    Toast.makeText(RecordYourOwnActivity.this, "", Toast.LENGTH_SHORT).show();
                    large_font.setImageResource(large_font_btn);
                    isPressed = false;
                } else {
                    isPressed = true;
//                    Toast.makeText(RecordYourOwnActivity.this, "", Toast.LENGTH_SHORT).show();
                    large_font.setImageResource(small_font_btn);
                }
            }
        });
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasMicrophone()) {
                    ShowPopup(v);
                } else {
                    Toast.makeText(RecordYourOwnActivity.this, "Microphone not found!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        home_btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordYourOwnActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        lib_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordYourOwnActivity.this, LibraryActivity.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupSignInSignUp(v);
            }
        });

        bg_music_btn = findViewById(R.id.bg_music_btn);

        intent = new Intent(RecordYourOwnActivity.this, StartRecordingActivity.class);
        intent.putExtra("music", true);

        bg_music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isPressed) {

                    intent.putExtra("music", false);

                    bg_music_btn.setImageResource(bg_music_off_btn);
                    isPressed = false;

                } else {
                    isPressed = true;
                    intent.putExtra("music", true);

                    bg_music_btn.setImageResource(bg_music_on_btn);
                }

            }
        });

    }

    public void ShowPopup(final View v) {
        myDialog.setContentView(R.layout.custom_record_timer);
        imageView = myDialog.findViewById(R.id.counter_image);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        handler.postDelayed(new Runnable() {
            public void run() {
                if (i < 3) {
                    imageView.setImageResource(count_down_timer_img[i]);
                    i++;
                }
                handler.postDelayed(this, 1000);

            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intent);

                finish();
                myDialog.dismiss();
            }
        }, 3 * 1100);
        myDialog.show();
    }

    public void ShowPopupSignInSignUp(View v) {

        myDialog.setContentView(R.layout.custom_popup);
        popup_login = myDialog.findViewById(R.id.select_login);
        popup_signup = myDialog.findViewById(R.id.sign_upact_btn);
        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(RecordYourOwnActivity.this, SignUpActivity.class));
            }
        });
        popup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                showLoginPopUp(v);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }

    public void showLoginPopUp(View v) {

        myDialog.setContentView(R.layout.custom_login_popup);

        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);

        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

}
