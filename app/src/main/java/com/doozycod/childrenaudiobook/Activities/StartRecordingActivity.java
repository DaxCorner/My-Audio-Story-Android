package com.doozycod.childrenaudiobook.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.Models.SharedModel;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.doozycod.childrenaudiobook.Utils.Upload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cz.msebera.android.httpclient.util.TextUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class StartRecordingActivity extends AppCompatActivity {

    ImageView start_personal_greeting, save_story_btn, share_story_btn, stop_recording_btn,
            imageView, stop_recorder_btn, login_dialog, popup_login, popup_signup,
            home_btn_recording, lib_btn_recording, login_btn_recording, home_btn_recorded, lib_btn_recorded, login_btn_recorded;
    Dialog myDialog;
    RelativeLayout start_recording_layout, save_recording_layout;
    int i = 0;
    String audioFilePath = "";
    boolean isRecording = false;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    int[] count_down_timer_img = {R.drawable.countdown_29, R.drawable.countdown_28, R.drawable.countdown_27, R.drawable.countdown_26, R.drawable.countdown_25
            , R.drawable.countdown_24, R.drawable.countdown_23, R.drawable.countdown_22, R.drawable.countdown_21, R.drawable.countdown_20, R.drawable.countdown_19,
            R.drawable.countdown_18, R.drawable.countdown_17, R.drawable.countdown_16, R.drawable.countdown_15, R.drawable.countdown_14,
            R.drawable.countdown_13, R.drawable.countdown_12, R.drawable.countdown_11, R.drawable.countdown_10, R.drawable.countdown_09, R.drawable.countdown_08,
            R.drawable.countdown_07, R.drawable.countdown_06, R.drawable.countdown_05, R.drawable.countdown_04, R.drawable.countdown_03, R.drawable.countdown_02,
            R.drawable.countdown_01, R.drawable.countdown_00};
    File mydir;
    File mydirRecording;
    boolean background_music;
    APIService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    String book_id;
    String android_id;
    Bundle extra;


    private static final String TAG = StartRecordingActivity.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL = "http://www.doozycod.in/books-manager/api/Library/add-library.php";


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_recording);

        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        extra = getIntent().getExtras();
        mediaPlayer = new MediaPlayer();
        mediaRecorder = new MediaRecorder();
        book_id = getIntent().getStringExtra("book_id");

        Log.e("Book ID and User_id", book_id + sharedPreferenceMethod.getUserId());

        if (book_id != null) {
            Toast.makeText(this, book_id, Toast.LENGTH_SHORT).show();
        }
        apiService = ApiUtils.getAPIService();
        myDialog = new Dialog(this);


        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        start_personal_greeting = findViewById(R.id.record_personal_msg);
        start_recording_layout = findViewById(R.id.recording_layout);
        save_recording_layout = findViewById(R.id.save_share_layout);
        home_btn_recording = findViewById(R.id.home_btn_start_recording);
        home_btn_recorded = findViewById(R.id.home_btn_recorded);
        lib_btn_recording = findViewById(R.id.lib_btn_recording);
        lib_btn_recorded = findViewById(R.id.lib_btn_recorded);
        login_btn_recording = findViewById(R.id.login_btn_recording);
        login_btn_recorded = findViewById(R.id.login_btn_recorded);
        stop_recording_btn = findViewById(R.id.stop_recording_btn);
        save_story_btn = findViewById(R.id.save_story_btn);
        share_story_btn = findViewById(R.id.share_story_btn_on_end);


        if (sharedPreferenceMethod != null) {
            if (!sharedPreferenceMethod.checkLogin()) {
                login_btn_recording.setImageResource(R.drawable.profile_btn_pressed);
                login_btn_recording.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StartRecordingActivity.this, ProfileActivity.class));
                    }
                });

                login_btn_recorded.setImageResource(R.drawable.profile_btn_pressed);
                login_btn_recorded.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StartRecordingActivity.this, ProfileActivity.class));
                    }
                });
            } else {
                login_btn_recording.setImageResource(R.drawable.login_btn_pressed);
                login_btn_recording.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShowPopupSignInSignUp();

                    }
                });
                login_btn_recorded.setImageResource(R.drawable.login_btn_pressed);
                login_btn_recorded.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShowPopupSignInSignUp();

                    }
                });
            }
        }

        mydir = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/");

        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        //Creating an internal dir;
        mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");

        if (!mydirRecording.exists()) {
            mydirRecording.mkdirs();
        }

        save_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveRecording();
            }
        });

        lib_btn_recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    stopRecording();
                    stopBGMusic();
                }
                startActivity(new Intent(StartRecordingActivity.this, LibraryActivity.class));

            }
        });

        home_btn_recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartRecordingActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        share_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShareRecording();
            }
        });

        background_music = getIntent().getExtras().getBoolean("music");
        Log.e("background_music =====", background_music + "");


        if (extra.getBoolean("yes")) {
            ShowPopupGreeting();

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recordAudio("static recorded story");
                    if (background_music) {
                        playBGMusic();

                    }

                }
            }, 500);
        }

        start_personal_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordPersonalGreetingPopUp();
            }
        });

        stop_recording_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_recording_layout.setVisibility(View.GONE);
                stopRecording();
                save_recording_layout.setVisibility(View.VISIBLE);
                stop_recording_btn.setEnabled(false);

                if (background_music) {
                    stopBGMusic();
                }


            }
        });
        home_btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    stopRecording();
                    stopBGMusic();
                }
                Intent intent = new Intent(StartRecordingActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        lib_btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartRecordingActivity.this, LibraryActivity.class));
            }
        });


    }

    public void recordAudio(String audio_filename) {

        isRecording = true;
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

        try {
            AssetFileDescriptor afd = getAssets().openFd("raw/morning.mp3");


            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();


        } catch (IOException e) {
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

    public void stopRecording() {


        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }

    public void ShowPopupGreeting() {

        myDialog.setContentView(R.layout.custom_personal_greet_popup);
        imageView = myDialog.findViewById(R.id.counter_image_personal);
        stop_recorder_btn = myDialog.findViewById(R.id.stop_recorder_btn);

        final Handler handler = new Handler();
        Runnable myRunnable = new Runnable() {
            public void run() {
                if (i < 30) {
                    imageView.setImageResource(count_down_timer_img[i]);
                    i++;
                }
                handler.postDelayed(this, 1000);

            }
        };

        handler.postDelayed(myRunnable, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


//                startActivity(new Intent(StartRecordingActivity.this, SaveShareYourStoryActivity.class));
                recordAudio("greeting");
                myDialog.dismiss();
            }
        }, 30 * 1050);
        myDialog.show();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        stop_recorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBGMusic();
                stopRecording();
                myDialog.dismiss();

                recordAudio("static recorded story");
                handler.postDelayed(myRunnable, 100);

            }
        });
        myDialog.show();
    }

    public void ShowPopup() {
        myDialog.setContentView(R.layout.custom_record_timer);
        imageView = myDialog.findViewById(R.id.counter_image);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        final Handler handler = new Handler();
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
                myDialog.dismiss();
//                playBGMusic();
                ShowPopupGreeting();
//                finish();
            }
        }, 3 * 1100);
        myDialog.show();
    }

    public void RecordPersonalGreetingPopUp() {

        myDialog.setContentView(R.layout.custom_yes_or_no_greeting);
        TextView greeting_dialog_txt = myDialog.findViewById(R.id.record_personal_greeting);
        ImageView record_greeting = myDialog.findViewById(R.id.yes_btn_record_greeting);
        ImageView donot_record_greeting = myDialog.findViewById(R.id.no_btn_record_greeting);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        greeting_dialog_txt.setTypeface(custom_font);
        myDialog.show();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        donot_record_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });
        record_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ShowPopupGreeting();

            }
        });
        myDialog.show();
    }

    public void ShowPopupSignInSignUp() {

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

        EditText et_email_btn = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_btn = myDialog.findViewById(R.id.et_password_dialog);
        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_btn.getText().toString().equals("") || et_email_btn.getText().toString().equals("")) {
                    Toast.makeText(StartRecordingActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
                } else {
                    String login_email = et_email_btn.getText().toString();
                    String login_password = et_password_btn.getText().toString();
                    loginRequest(login_email, login_password);
                }

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }


    public void loginRequest(String entered_email, String entered_password) {
        apiService.signIn(entered_email, entered_password, android_id).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {

                        sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                        Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id());
                        myDialog.dismiss();
                        sharedPreferenceMethod.saveLogin(true);
                        login_btn_recording.setImageResource(R.drawable.profile_btn_pressed);
                        login_btn_recorded.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_btn_recording.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(StartRecordingActivity.this, ProfileActivity.class));
                                }
                            });
                            login_btn_recorded.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(StartRecordingActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            ShowPopupSignInSignUp();
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");

            }

        });
    }

    void saveRecording() {

        final EditText editText = findViewById(R.id.name_recorded_story);
        String audio_filename = editText.getText().toString();


        if (!audio_filename.equals("")) {

            mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");
            if (mydirRecording.exists()) {
                File from = new File(mydirRecording, "static recorded story.mp3");
                File fromGreeting = new File(mydirRecording, "greeting.mp3");
                File to = new File(mydirRecording, audio_filename + ".mp3");


                String audioFile = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/" + audio_filename + ".mp3";
                String greetingFile = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/" + "greeting-" + audio_filename + ".mp3";
                String fromgreet = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/" + "greeting.mp3";

                File toGreet = new File(mydirRecording, "greeting-" + audio_filename + ".mp3");
                if (from.exists() && fromGreeting.exists() || from.exists()) {
                    from.renameTo(to);
                    fromGreeting.renameTo(toGreet);
                    SharedModel sharedModel = new SharedModel();
                    if (toGreet.exists()) {
//                        uploadFile(audioFilePath);
                        uploadAudioToServer(audioFile, greetingFile, sharedPreferenceMethod.getUserId(), audio_filename, book_id);

                    } else {
//                        uploadFile(audioFilePath);
                        uploadAudioToServer(audioFile, fromgreet, sharedPreferenceMethod.getUserId(), audio_filename, book_id);
                    }
                }

            }
//            startActivity(new Intent(StartRecordingActivity.this, RecordYourOwnActivity.class));
//            finish();

        } else {
            Toast.makeText(this, "Please enter story name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAudioToServer(String pathToAudioFile, String greetingPath, String user_id, String name, String book_id) {

        File audioFile = new File(pathToAudioFile);
        File greetingFile = new File(greetingPath);
        Log.e("User ID", user_id);
        Log.e("Audio Path Split", audioFile.getName());
//        Log.e("FIle PATH -->", audioFile + "\n" + greetingFile + "\n" + sharedPreferenceMethod.getUserId() + "\n" + audioFile + "\n" + book_id);

        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, user_id);
        RequestBody userName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody bookId = RequestBody.create(okhttp3.MultipartBody.FORM, book_id);

        RequestBody greetingBody = RequestBody.create(MediaType.parse("multipart/form-data"), greetingFile);
        RequestBody audioBody = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
        MultipartBody.Part greeting = MultipartBody.Part.createFormData("audio_message", audioFile.getName(), audioBody);
        MultipartBody.Part audiofile = MultipartBody.Part.createFormData("audio_story", audioFile.getName(), audioBody);

        Call<ResultObject> serverCom = apiService.uploadAudioToServer(userId, bookId, userName, greeting, audiofile);

        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if (response.isSuccessful()) {
                    Log.e("Response", response.body().getSuccess() + response.body().getMessage());
                }
                if (!TextUtils.isEmpty(result.getSuccess())) {
                    Toast.makeText(StartRecordingActivity.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
                    Log.d("Recording Result", "Result " + result.getSuccess());
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d("Error Result", "Error message " + t.getMessage());
            }
        });
    }


    private void saveShareRecording() {

        final EditText editText = findViewById(R.id.name_recorded_story);
        String audio_filename = editText.getText().toString();
        if (!audio_filename.equals("")) {

            mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");
            String audioFile = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/" + audio_filename + ".mp3";
            String greetingfile = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/greeting.mp3";

            if (mydirRecording.exists()) {
                File from = new File(mydirRecording, "static recorded story.mp3");
                File fromGreeting = new File(mydirRecording, "greeting.mp3");
                File to = new File(mydirRecording, audio_filename + ".mp3");


                String greetingFile = "/storage/emulated/0/myAudioBook/audioBooks/temp/recording/" + "greeting-" + audio_filename + ".mp3";

                File toGreet = new File(mydirRecording, "greeting-" + audio_filename + ".mp3");
                if (from.exists() && fromGreeting.exists() || from.exists()) {
                    from.renameTo(to);
                    fromGreeting.renameTo(toGreet);
                    if (toGreet.exists()) {
                        uploadAudioToServer(audioFile, greetingFile, "", "", "");


                    } else {
                        uploadAudioToServer(audioFile, "", "", "", "");
                    }
                }

            }
            startActivity(new Intent(StartRecordingActivity.this, ShareYourStoryActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Please enter story name!", Toast.LENGTH_SHORT).show();
        }
    }


//    private void uploadAudioWithGreeting(String pathToAudioFile, String greetingPath) {
//        File audioFile = new File(pathToAudioFile);
//        File greetingFile = new File(greetingPath);
//        RequestBody greetingBody = RequestBody.create(MediaType.parse("audio/*"), greetingFile);
//        RequestBody audioBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
//        MultipartBody.Part greeting = MultipartBody.Part.createFormData("audio", audioFile.getName(), greetingBody);
//        MultipartBody.Part audiofile = MultipartBody.Part.createFormData("audio", audioFile.getName(), audioBody);
//
//        Call<ResultObject> serverCom = apiService.uploadAudioWithGreeting(greeting, audiofile);
//        serverCom.enqueue(new Callback<ResultObject>() {
//            @Override
//            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                ResultObject result = response.body();
//                if (!TextUtils.isEmpty(result.getSuccess())) {
//
//                    Toast.makeText(StartRecordingActivity.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
//                    Log.d("Recording Result", "Result " + result.getSuccess());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultObject> call, Throwable t) {
//                Log.d("Error Result", "Error message " + t.getMessage());
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        stopBGMusic();
        stopRecording();

        mydirRecording = new File(Environment.getExternalStorageDirectory() + "/myAudioBook/audioBooks/temp/recording/");
        if (mydirRecording.exists()) {
            File from = new File(mydirRecording, "static recorded story.mp3");
            if (from.exists())
                from.delete();
        }
        super.onBackPressed();

    }

}
