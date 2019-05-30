package com.doozycod.childrenaudiobook.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;
import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class BookDetailActivity extends AppCompatActivity {
    int[] count_down_timer_img = {R.drawable.countdown_03, R.drawable.countdown_02,
            R.drawable.countdown_01, R.drawable.countdown_00};
    ImageView recordAudioButton, home_btn_listen_audio, library_btn_listen, login_btn_listen, popup_login, popup_signup, login_dialog, listen_book, use_bg_music;
    Dialog myDialog;
    String AudioSavePathInDevice, audioFilePath;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    int length;
    int i = 0;

    boolean isPlaying = true;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;
    APIService apiService;
    Bundle bundle;
    SharedPreferenceMethod sharedPreferenceMethod;
    String android_id;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    String PRODUCT_ID = "purchase_book";
    String book_id, user_id, is_paid, book_image, book_name;
    CustomProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getIntent().getExtras();
        progressDialog = new CustomProgressBar(this);
        inAppBilling();
        setContentView(R.layout.activity_listen_audio_story);
        login_btn_listen = findViewById(R.id.login_btn_listen);
        recordAudioButton = findViewById(R.id.record_audio);
        listen_book = findViewById(R.id.listen_book);
        use_bg_music = findViewById(R.id.use_background_audio);
        home_btn_listen_audio = findViewById(R.id.home_btn_listen_audio);
        library_btn_listen = findViewById(R.id.lib_btn_listen_audio);
        apiService = ApiUtils.getAPIService();

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
//        Check user Logged in or Not
        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
//                ShowProgressDialog();

                login_btn_listen.setImageResource(R.drawable.login_btn_pressed);

                login_btn_listen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            } else {
//                ShowProgressDialog();
                login_btn_listen.setImageResource(R.drawable.profile_btn_pressed);
                login_btn_listen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BookDetailActivity.this, ProfileActivity.class));
                    }
                });

            }
        }
        String audio_file = bundle.getString("audio_file");
        book_id = bundle.getString("book_id");
        user_id = bundle.getString("user_id");
        is_paid = bundle.getString("is_paid");

        myDialog = new Dialog(this);

        recordAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BillingProcessor.isIabServiceAvailable(BookDetailActivity.this)) {
                    Toast.makeText(BookDetailActivity.this, "In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16", Toast.LENGTH_SHORT).show();
                }

                bp.getPurchaseTransactionDetails(getString(R.string.license_key));
                SkuDetails sku = bp.getPurchaseListingDetails(PRODUCT_ID);
                Log.e("Purchased History", PRODUCT_ID + " is purchased   " + bp.isPurchased(PRODUCT_ID));
                Log.e("Purchased History", sku != null ? sku.toString() : "Failed to load SKU details");
                if (sharedPreferenceMethod.checkLogin()) {

                    Toast.makeText(BookDetailActivity.this, "You must have to login first!", Toast.LENGTH_SHORT).show();
                    showLoginPopUp(v);

                } else {

                    if (is_paid.equals("1")) {
                        Intent intent = new Intent(BookDetailActivity.this, StartRecordingActivity.class);
                        Toast.makeText(BookDetailActivity.this, audio_file + book_id + user_id + is_paid, Toast.LENGTH_SHORT).show();
                        Bundle extras = new Bundle();
                        extras.putString("audio_file", audio_file);
                        extras.putString("book_id", book_id);
                        extras.putString("user_id", user_id);
                        extras.putString("is_paid", is_paid);
                        intent.putExtras(extras);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                    ShowCountTimer();
//                    startActivity(intent);

                    } else {
                        if (bp.isPurchased(PRODUCT_ID)) {
                            Log.e("Book is Purchased", "Book is Purchased!");
                        } else {

                            if (bp.isPurchased(PRODUCT_ID)) {
                                bookPurchased();
                            }
                            bp.purchase(BookDetailActivity.this, PRODUCT_ID);
                            Intent intent = new Intent(BookDetailActivity.this, StartRecordingActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("audio_file", audio_file);
                            extras.putString("book_id", book_id);
                            extras.putString("user_id", user_id);
                            extras.putString("is_paid", is_paid);
                            intent.putExtras(extras);
                            bp.consumePurchase(PRODUCT_ID);


                        }
                    }
                }
            }
        });

        listen_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowProgressDialog();
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

    public void ShowProgressDialog() {
        progressDialog.showProgress();
    }

    public void HideProgressDialog() {
        progressDialog.hideProgress();

    }

    public void inAppBilling() {
        bp = new BillingProcessor(this, null, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                Toast.makeText(BookDetailActivity.this, productId + "  " + details, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onPurchaseHistoryRestored() {
                for (String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);

            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {

            }

            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
//                Toast.makeText(BookDetailActivity.this, "onBillingInitialized", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void ShowPopup() {

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

    public void bookPurchased() {
        apiService.PaidBooks(sharedPreferenceMethod.getUserId(), book_id).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                Log.e("Response PaidBooks", response.body().getSuccess() + "  " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void ShowMediaPlayerPopoup() {
        SeekBar seekBar;
        Dialog myDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        myDialog.setContentView(R.layout.custom_popup_media_player);

        ImageView play_btn = myDialog.findViewById(R.id.play_pause_btn);
        ImageView rewind_btn = myDialog.findViewById(R.id.rewind_btn);
        ImageView ff_btn = myDialog.findViewById(R.id.fast_forward);
        ImageView book_img = myDialog.findViewById(R.id.book_img_player);
        ImageView back_btn = myDialog.findViewById(R.id.back_press_player);
        TextView audio_fileName = myDialog.findViewById(R.id.audio_file_name);

        String book_image = bundle.getString("player_book_img");
        String book_name = bundle.getString("player_book_name");

        Log.e("Bundle Data", book_image + book_name);
        Glide.with(this).load("http://" + book_image).into(book_img);
        audio_fileName.setText(book_name);

        seekBar = myDialog.findViewById(R.id.seekbar);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        audio_fileName.setTypeface(custom_font);
        audio_fileName.setText(book_name);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGMusic();
                myDialog.dismiss();

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
            HideProgressDialog();
            playBGMusic(seekBar, play_btn);

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
        EditText et_email_btn = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_btn = myDialog.findViewById(R.id.et_password_dialog);
        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_btn.getText().toString().equals("") || et_password_btn.getText().toString().equals("")) {
                    Toast.makeText(BookDetailActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
                } else {
                    String pass = et_password_btn.getText().toString();
                    if (pass.length() > 6) {
                        String login_email = et_email_btn.getText().toString();
                        String login_password = et_password_btn.getText().toString();
                        ShowProgressDialog();
                        loginRequest(login_email, login_password);

                    } else {
                        Toast.makeText(BookDetailActivity.this, "Password is at least 7 words!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    public void playBGMusic(SeekBar seekBar, ImageView play_btn) {

        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = getAssets().openFd("raw/eli_s_star.mp3");

            String audio = bundle.getString("audio_file");

            Log.e("Audio_File_from", audio);
            Log.e("Book_image_act", bundle.getString("player_book_img"));
            mediaPlayer.setDataSource("http://" + audio);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Handler mHandler = new Handler();
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        seekBar.setProgress(mCurrentPosition);
                        if (mCurrentPosition == mediaPlayer.getDuration() / 1000) {
                            play_btn.setImageResource(R.drawable.play_button);
                        }
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loginRequest(String entered_email, String entered_password) {
        apiService.signIn(entered_email, entered_password, android_id).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {
                HideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {

                        sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                        Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id() + "\n" + response.body().getDevice_id());
                        sharedPreferenceMethod.saveLogin(true);
                        sharedPreferenceMethod.login(sharedPreferenceMethod.getUserId());
                        myDialog.dismiss();
                        login_btn_listen.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_btn_listen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(BookDetailActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            ShowPopup();
                        }

                    } else {
                        Toast.makeText(BookDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");
                HideProgressDialog();

            }

        });
    }

    @Override
    public void onBackPressed() {
        stopBGMusic();
        bp.release();
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

}
