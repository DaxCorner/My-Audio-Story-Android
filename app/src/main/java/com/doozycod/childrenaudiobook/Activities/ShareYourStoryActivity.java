package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ShareStoryModel;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class ShareYourStoryActivity extends AppCompatActivity {
    ImageView home_btn_share, library_btn_share, login_btn_share, login_dialog, popup_login, popup_signup, share_your_story;
    Dialog myDialog;
    APIService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    String android_id;
    CustomProgressBar progressDialog;
    RequestBody greetingBody;
    EditText email_to_share, phone_no_to_share;
    Bundle extras;
    String book_id, audioFile, greetingFile, audio_filename, share_on_email, share_on_phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_story);
        apiService = ApiUtils.getAPIService();
        progressDialog = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        extras = getIntent().getExtras();

        home_btn_share = findViewById(R.id.home_btn_share);
        library_btn_share = findViewById(R.id.lib_btn_share_story);
        login_btn_share = findViewById(R.id.login_btn_share);
        share_your_story = findViewById(R.id.share_story_btn);
        email_to_share = findViewById(R.id.share_on_email_et);
        phone_no_to_share = findViewById(R.id.share_on_phone_text_et);
        TextView tx = findViewById(R.id.email);
        TextView phone_no = findViewById(R.id.phone_no);

        myDialog = new Dialog(this);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        tx.setTypeface(custom_font);
        phone_no.setTypeface(custom_font);

        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
//                ShowProgressDialog();

                login_btn_share.setImageResource(R.drawable.login_btn_pressed);

                login_btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            } else {
//                ShowProgressDialog();
                login_btn_share.setImageResource(R.drawable.profile_btn_pressed);
                login_btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ShareYourStoryActivity.this, ProfileActivity.class));
                    }
                });

            }
        }


        book_id = extras.getString("book_id");
        audioFile = extras.getString("audioFile");
        greetingFile = extras.getString("greetingFile");
        audio_filename = extras.getString("audio_filename");

        share_on_phone_no = phone_no_to_share.getText().toString();


        home_btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareYourStoryActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Log.e("BUNDLE PARSED DATA", sharedPreferenceMethod.getUserId() + " \n" + book_id + " \n" + audio_filename + " \n" + greetingFile + " \n" + audioFile);

        library_btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareYourStoryActivity.this, LibraryActivity.class));

            }
        });

        share_your_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(email_to_share.getText().toString()) || TextUtils.isEmpty(phone_no_to_share.getText().toString())) {

                    if (!email_to_share.getText().toString().equals("")) {
                        share_on_email = email_to_share.getText().toString();
                        if (greetingFile.equals("false")) {
                            shareAudioToUser(sharedPreferenceMethod.getUserId(), book_id, audio_filename, "", audioFile, share_on_phone_no, share_on_email);
                        } else {
                            shareAudioToUser(sharedPreferenceMethod.getUserId(), book_id, audio_filename, greetingFile, audioFile, share_on_phone_no, share_on_email);
                        }
                    } else if (!phone_no_to_share.getText().toString().equals("")) {
                        if (greetingFile.equals("false")) {
                            shareAudioToUser(sharedPreferenceMethod.getUserId(), book_id, audio_filename, "", audioFile, share_on_phone_no, "");
                        } else {
                            shareAudioToUser(sharedPreferenceMethod.getUserId(), book_id, audio_filename, greetingFile, audioFile, share_on_phone_no, "");
                        }

                    } else {
                        Toast.makeText(ShareYourStoryActivity.this, "Select email or phone number", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(ShareYourStoryActivity.this, "Enter only Email or Phone !", Toast.LENGTH_SHORT).show();

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

    public void ShowPopup() {

        myDialog.setContentView(R.layout.custom_popup);
        popup_login = myDialog.findViewById(R.id.select_login);
        popup_signup = myDialog.findViewById(R.id.sign_upact_btn);

        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(ShareYourStoryActivity.this, SignUpActivity.class));
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
                    Toast.makeText(ShareYourStoryActivity.this, "Username and password can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String login_email = et_email_btn.getText().toString();
                    String login_password = et_password_btn.getText().toString();
                    ShowProgressDialog();
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
                        HideProgressDialog();
                        sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                        Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id());
                        sharedPreferenceMethod.saveLogin(true);
                        sharedPreferenceMethod.login(sharedPreferenceMethod.getUserId());
                        myDialog.dismiss();
                        login_btn_share.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_btn_share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(ShareYourStoryActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            ShowPopup();
                        }

                    } else {
                        errorDialogLogin();
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


    public void shareAudioToUser(String user_id, String book_id, String name, String audio_message, String audio_story, String phone_number, String email) {

        File audioFile = new File(audio_story);
        File greetingFile = new File(audio_message);
        Log.e("User ID", user_id);
        Log.e("Audio Path Split", audioFile.getName());

        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, user_id);
        RequestBody storyName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody bookId = RequestBody.create(okhttp3.MultipartBody.FORM, book_id);
        RequestBody sentToEmail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
        RequestBody phone_no = RequestBody.create(okhttp3.MultipartBody.FORM, phone_number);

        if (audio_message.equals("")) {
            greetingBody = RequestBody.create(MediaType.parse("multipart/form-data"), audio_message);
        } else {
            greetingBody = RequestBody.create(MediaType.parse("multipart/form-data"), greetingFile);
        }
        RequestBody audioBody = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
        MultipartBody.Part greeting = MultipartBody.Part.createFormData("audio_message", greetingFile.getName(), greetingBody);
        MultipartBody.Part audiofile = MultipartBody.Part.createFormData("audio_story", audioFile.getName(), audioBody);
        apiService.shareLibraryToUser(userId, storyName, bookId, phone_no, sentToEmail, greeting, audiofile).enqueue(new Callback<ShareStoryModel>() {

            @Override
            public void onResponse(Call<ShareStoryModel> call, retrofit2.Response<ShareStoryModel> response) {
                HideProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("true")) {
                        Toast.makeText(ShareYourStoryActivity.this, "Audio Story Shared!", Toast.LENGTH_SHORT).show();
//                        Log.e("RESPONSE ::", response.body().getMessage().notification.getTitle() + "\n" + response.body().getMessage().notification.getBody());

                    } else {
                        Log.e("RESPONSE ::", response.body().getStatus() + "\n" + response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<ShareStoryModel> call, Throwable t) {

            }


        });

    }

    public void errorDialogLogin() {

        Dialog errorDialog = new Dialog(ShareYourStoryActivity.this);
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
}
