package com.doozycod.childrenaudiobook.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.doozycod.childrenaudiobook.R;

import java.io.IOException;

import static com.doozycod.childrenaudiobook.R.drawable.bg;
import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class BookDetailActivity extends AppCompatActivity {
    ImageView recordAudioButton, home_btn_listen_audio, library_btn_listen, login_btn_listen, popup_login, popup_signup, login_dialog, listen_book, use_bg_music;
    Dialog myDialog;
    String AudioSavePathInDevice, audioFilePath;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    int length;
    boolean isPlaying = true;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;

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

        myDialog = new Dialog(this);

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


                ShowMediaPlayerPopoup();


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
                Intent intent = new Intent(BookDetailActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    void stopBGMusic() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    public void ShowMediaPlayerPopoup() {
        SeekBar seekBar;
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custom_popup_media_player);

        ImageView play_btn = myDialog.findViewById(R.id.play_pause_btn);
        ImageView rewind_btn = myDialog.findViewById(R.id.rewind_btn);
        ImageView ff_btn = myDialog.findViewById(R.id.fast_forward);
        seekBar = myDialog.findViewById(R.id.seekbar);
//        TextView audio_file_name = myDialog.findViewById(R.id.audio_file_name);
        seekBar = myDialog.findViewById(R.id.seekbar);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

//        audio_file_name.setTypeface(custom_font);
//        audio_file_name.setText(audioFileName);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });
        myDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    stopBGMusic();


                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            playBGMusic(seekBar);
        }


        ff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }

            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    length = mediaPlayer.getCurrentPosition();
                    play_btn.setImageResource(R.drawable.play_button);

                } else {
                    play_btn.setImageResource(R.drawable.pause_button);

                    mediaPlayer.seekTo(length);
                    mediaPlayer.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));
                    }
                }

            }
        });

        rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (currentPosition - seekBackwardTime >= 0) {

                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }


            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(bg));

        myDialog.show();
    }


    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
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

    public void playBGMusic(SeekBar seekBar) {

        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = getAssets().openFd("raw/eli_s_star.mp3");


            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Handler mHandler = new Handler();
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setMax(mediaPlayer.getDuration()/1000);
                        seekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        stopBGMusic();
        super.onBackPressed();
    }


}
