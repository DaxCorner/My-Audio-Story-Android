package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class StartRecordingActivity extends AppCompatActivity {
    ImageView start_personal_greeting, save_story_btn, share_story_btn, stop_recording_btn, imageView, stop_recorder_btn, login_dialog, popup_login, popup_signup, home_btn_recording, lib_btn_recording, login_btn_recording;
    Dialog myDialog;
    RelativeLayout start_recording_layout, save_recording_layout;
    int i = 0;
    String audioFilePath = "";
    boolean isRecording = false;
    boolean bg_music = true;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    int[] count_down_timer_img = {R.drawable.countdown_29, R.drawable.countdown_28, R.drawable.countdown_27, R.drawable.countdown_26, R.drawable.countdown_25
            , R.drawable.countdown_24, R.drawable.countdown_23, R.drawable.countdown_22, R.drawable.countdown_21, R.drawable.countdown_20, R.drawable.countdown_19,
            R.drawable.countdown_18, R.drawable.countdown_17, R.drawable.countdown_16, R.drawable.countdown_15, R.drawable.countdown_14,
            R.drawable.countdown_13, R.drawable.countdown_12, R.drawable.countdown_11, R.drawable.countdown_10, R.drawable.countdown_09, R.drawable.countdown_08,
            R.drawable.countdown_07, R.drawable.countdown_06, R.drawable.countdown_05, R.drawable.countdown_04, R.drawable.countdown_03, R.drawable.countdown_02,
            R.drawable.countdown_01, R.drawable.countdown_00};
    View view;
    File mydir;
    File mydirRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_recording);

        view = new View(this);
        myDialog = new Dialog(this);

        start_personal_greeting = findViewById(R.id.record_personal_msg);
        start_recording_layout = findViewById(R.id.recording_layout);
        save_recording_layout = findViewById(R.id.save_share_layout);
        home_btn_recording = findViewById(R.id.home_btn_start_recording);
        lib_btn_recording = findViewById(R.id.lib_btn_recording);
        login_btn_recording = findViewById(R.id.login_btn_recording);
        stop_recording_btn = findViewById(R.id.stop_recording_btn);
        save_story_btn = findViewById(R.id.save_story_btn);
        share_story_btn = findViewById(R.id.share_story_btn_on_end);
        mydir = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/");

        if (!mydir.exists()) {
            mydir.mkdirs();
        }//Creating an internal dir;

        mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");
        if (!mydirRecording.exists()) {
            mydirRecording.mkdirs();
        }//Creating an internal dir;

        save_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveRecording();
            }
        });

        share_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecording();
                startActivity(new Intent(StartRecordingActivity.this, ShareStory.class));
            }
        });
        boolean intent = getIntent().getExtras().getBoolean("music");
        Log.e("String Intent =====", intent + "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordAudio("static recorded story");
                if (bg_music == intent) {
                    playBGMusic();

                } else {

                }

            }
        }, 500);

        start_personal_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });

        stop_recording_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_recording_layout.setVisibility(View.GONE);
                stopRecording();
                save_recording_layout.setVisibility(View.VISIBLE);
                stop_recording_btn.setEnabled(false);
                boolean intent = getIntent().getExtras().getBoolean("music");
                if (bg_music == intent) {
                    stopBGAudio();
                }


            }
        });
        home_btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartRecordingActivity.this, MainActivity.class));
            }
        });
        lib_btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartRecordingActivity.this, LibraryActivity.class));
            }
        });
        login_btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupSignInSignUp(v);
            }
        });


    }

    public void recordAudio(String audio_filename) {

        isRecording = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(128000);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(mydirRecording + "/" + audio_filename + ".mp3");
        try {

            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBGMusic() {

        audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "" + "/morning.mp3";
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("raw/morning.mp3");


            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void stopBGAudio() {
        mediaRecorder.release();
        mediaRecorder = null;

    }

    public void stopRecording() {


        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }

    public void ShowPopup() {

        myDialog.setContentView(R.layout.custom_personal_greet_popup);
        imageView = myDialog.findViewById(R.id.counter_image_personal);
        stop_recorder_btn = myDialog.findViewById(R.id.stop_recorder_btn);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (i < 30) {
                    imageView.setImageResource(count_down_timer_img[i]);
                    i++;
                }
                handler.postDelayed(this, 1000);

            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartRecordingActivity.this, RecordedAudioActivity.class));
                finish();
                myDialog.dismiss();
            }
        }, 30 * 1050);
        myDialog.show();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        stop_recorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                handler.removeCallbacksAndMessages(null);
            }
        });
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
                startActivity(new Intent(StartRecordingActivity.this, SignUpActivity.class));
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


    void saveRecording() {
        final EditText editText = findViewById(R.id.name_recorded_story);
        String audio_filename = editText.getText().toString();
        if (!audio_filename.equals("")) {

            mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");
            if (mydirRecording.exists()) {
                File from = new File(mydirRecording, "static recorded story.mp3");
                File to = new File(mydirRecording, audio_filename + ".mp3");
                if (from.exists())
                    from.renameTo(to);
            }
        } else {
            Toast.makeText(this, "Please enter story name!", Toast.LENGTH_SHORT).show();
        }
    }
}
