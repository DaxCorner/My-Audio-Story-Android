package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.doozycod.childrenaudiobook.Helper.Permissions;
import com.doozycod.childrenaudiobook.R;

import java.io.IOException;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class BookDetailActivity extends AppCompatActivity {
    ImageView recordAudioButton, home_btn_listen_audio, library_btn_listen, login_btn_listen, popup_login, popup_signup, login_dialog, listen_book, use_bg_music;
    Dialog myDialog;
    String AudioSavePathInDevice;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_listen_audio_story);
        recordAudioButton = findViewById(R.id.record_audio);
        login_btn_listen = findViewById(R.id.login_btn_listen);
        listen_book = findViewById(R.id.listen_book);
        use_bg_music = findViewById(R.id.use_background_audio);
        home_btn_listen_audio = findViewById(R.id.home_btn_listen_audio);
        library_btn_listen = findViewById(R.id.lib_btn_listen_audio);


        recordAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookDetailActivity.this, RecordYourOwnActivity.class));
            }
        });
        login_btn_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        listen_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        use_bg_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        home_btn_listen_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookDetailActivity.this, ChooseYourBookActivity.class));
            }
        });
        library_btn_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookDetailActivity.this, LibraryActivity.class));
            }
        });
    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.custom_popup);
        popup_login = myDialog.findViewById(R.id.select_login);
        popup_signup = myDialog.findViewById(R.id.sign_upact_btn);
        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(BookDetailActivity.this, SignUpActivity.class));
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

    public void recordAudio() {
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        "" + "AudioRecording.3gp";

        MediaRecorderReady();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
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
}
