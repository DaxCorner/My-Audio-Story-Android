package com.doozycod.childrenaudiobook.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;

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
    APIService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    Bundle bundle;
    String android_id;
    CustomProgressBar progressDialog;
//    String PRODUCT_ID = "purchase_book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressDialog = new CustomProgressBar(this);
        myDialog = new Dialog(this);
        setContentView(R.layout.activity_record_own);

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        apiService = ApiUtils.getAPIService();
        record_btn = findViewById(R.id.recording_start);
        large_font = findViewById(R.id.large_font);
        lib_btn = findViewById(R.id.lib_btn_record_own);
        login_btn = findViewById(R.id.login_btn_record_your_own);
        home_btn_record = findViewById(R.id.home_btn_record_own);


        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
                ShowProgressDialog();

                login_btn.setImageResource(R.drawable.login_btn_pressed);

                login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup(v);
                    }
                });
            } else {
                ShowProgressDialog();
                login_btn.setImageResource(R.drawable.profile_btn_pressed);
                login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RecordYourOwnActivity.this, ProfileActivity.class));
                    }
                });

            }
        }

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
                    String is_paid = bundle.getString("is_paid");
                    if (is_paid.equals("1")) {
                        RecordPersonalGreetingPopUp();
                    } else {
                        Toast.makeText(RecordYourOwnActivity.this, "It's looks like you havn't paid for this :/", Toast.LENGTH_SHORT).show();
                    }
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

    public void ShowProgressDialog() {
        progressDialog.showProgress();
    }

    public void HideProgressDialog() {
        progressDialog.hideProgress();

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
                Intent intent = new Intent(RecordYourOwnActivity.this, StartRecordingActivity.class);
                String audio = bundle.getString("audio_file");
                String book_id = bundle.getString("book_id");
                String is_paid = bundle.getString("is_paid");

                intent.putExtra("audio_file", audio);
                intent.putExtra("book_id", book_id);
//                Toast.makeText(RecordYourOwnActivity.this, book_id, Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
                startActivity(intent);
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

                ShowPopup(v);


            }
        });
        record_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putBoolean("yes", true);

                intent.putExtras(bundle);

                startActivity(intent);
                myDialog.dismiss();


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

        EditText et_email_btn = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_btn = myDialog.findViewById(R.id.et_password_dialog);

        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_btn.getText().toString().equals("") || et_email_btn.getText().toString().equals("")) {
                    Toast.makeText(RecordYourOwnActivity.this, "Username and password can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String login_email = et_email_btn.getText().toString();
                    String login_password = et_password_btn.getText().toString();
                    generatePushToken();
                    loginRequest(login_email, login_password, sharedPreferenceMethod.getToken());
                }

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    public void generatePushToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        String token = task.getResult().getToken();
                        sharedPreferenceMethod.spSaveToken(token);
                        // Log and toast

                        Log.e("TOKEN", token);
                    }
                });
    }

    public void loginRequest(String entered_email, String entered_password, String token) {
        apiService.signIn(entered_email, entered_password, token, android_id).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {

                        sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                        Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id());
                        sharedPreferenceMethod.saveLogin(true);
                        myDialog.dismiss();
                        login_btn.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(RecordYourOwnActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            ShowPopupSignInSignUp();
                        }

                    } else {
                        errorDialogLogin();
                    }
                }

            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");

            }

        });
    }

    public void errorDialogLogin() {

        Dialog errorDialog = new Dialog(RecordYourOwnActivity.this);
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        errorDialog.setContentView(R.layout.error_dialog_login);


        errorDialog.show();

        ImageView back_arror_btn = errorDialog.findViewById(R.id.error_back_btn);
        back_arror_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
    }

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }


}
