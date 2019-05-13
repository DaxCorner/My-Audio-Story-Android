package com.doozycod.childrenaudiobook;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class RecorderActivtiy extends AppCompatActivity {
    String AudioSavePathInDevice = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button startRecording, stopRecording, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Permissions.Check_RECORD_AUDIO(RecorderActivtiy.this) && !Permissions.Check_STORAGE(RecorderActivtiy.this)) {

            Permissions.Request_STORAGE(RecorderActivtiy.this, 12);
            Permissions.Request_RECORD_AUDIO(RecorderActivtiy.this, 22);
        }
        setContentView(R.layout.activity_recorder_activtiy);
        startRecording = (Button) findViewById(R.id.button);
        stopRecording = (Button) findViewById(R.id.button2);

        stopRecording.setEnabled(false);

        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

                startRecording.setEnabled(false);
                stopRecording.setEnabled(true);

                Toast.makeText(RecorderActivtiy.this, "Recording started",
                        Toast.LENGTH_LONG).show();
            }


        });
        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                stopRecording.setEnabled(false);
//                buttonPlayLastRecordAudio.setEnabled(true);
                startRecording.setEnabled(true);
//                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(RecorderActivtiy.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

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

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
