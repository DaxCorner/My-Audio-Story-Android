package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Adapter.Login_ViewPagerAdapter;
import com.doozycod.childrenaudiobook.Adapter.RecyclerAdapter;
import com.doozycod.childrenaudiobook.Helper.Model;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.LibraryModel;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Service.Config;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hmomeni.progresscircula.ProgressCircula;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class LibraryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Dialog myDialog;
    ImageView login_dialog, login_btn_main, home_btn;
    private MediaPlayer mediaPlayer;
    RecyclerAdapter recyclerAdapter;
    APIService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    String android_id;
    List<LibraryModel.LibraryDetails> libraryModelList;
    CustomProgressBar progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressDialog = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        myDialog = new Dialog(this);

        recyclerView = findViewById(R.id.recycler_view_lib);
        recyclerView.setHasFixedSize(true);
        home_btn = findViewById(R.id.home_btn_lib);
        login_btn_main = findViewById(R.id.login_btn_lib);
        Log.e("User_id", sharedPreferenceMethod.getUserId());

        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
//                ShowProgressDialog();

                login_btn_main.setImageResource(R.drawable.login_btn_pressed);

                login_btn_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            } else {
//                ShowProgressDialog();
                login_btn_main.setImageResource(R.drawable.profile_btn_pressed);
                login_btn_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                    }
                });

            }
        }
        if (!sharedPreferenceMethod.checkLogin()) {
            getLibrary(sharedPreferenceMethod.getUserId());
            ShowProgressDialog();
        } else {
            Log.e("Library", "Nothing here");
        }


        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void ShowPopup() {
        myDialog.setContentView(R.layout.custom_popup);
        ImageView popup_login = myDialog.findViewById(R.id.select_login);
        ImageView popup_signup = myDialog.findViewById(R.id.sign_upact_btn);
        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(LibraryActivity.this, SignUpActivity.class));
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
                    Toast.makeText(LibraryActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
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
                HideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {

                        sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                        Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id());
                        sharedPreferenceMethod.saveLogin(true);
                        sharedPreferenceMethod.login(sharedPreferenceMethod.getUserId());
                        myDialog.dismiss();
                        login_btn_main.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_btn_main.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                                }
                            });
                        } else {
                            ShowPopup();
                        }

                    } else {
                        Toast.makeText(LibraryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    public void ShowProgressDialog() {
        progressDialog.showProgress();
    }

    public void HideProgressDialog() {
        progressDialog.hideProgress();

    }

    public void getLibrary(String userId) {
        apiService.getLibrary(userId).enqueue(new Callback<LibraryModel>() {
            @Override
            public void onResponse(Call<LibraryModel> call, Response<LibraryModel> response) {
                if (response.isSuccessful()) {
                    HideProgressDialog();
                    Log.e("Library JSON", response.body().getStatus() + "\n " + response.body().getLibrary_list_data());
                    if (response.body().getStatus().equals("true")) {
                        libraryModelList = response.body().getLibrary_list_data();
                        recyclerAdapter = new RecyclerAdapter(LibraryActivity.this, libraryModelList, apiService, sharedPreferenceMethod);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setLayoutManager(new LinearLayoutManager(LibraryActivity.this));
                        recyclerView.setAdapter(recyclerAdapter);
                        Log.e("lib book image", libraryModelList.get(0).book_details.getBook_image());

//                        Log.e("book image lib", response.body().LibraryDetails.book_details.getBook_image());
                    }
                }
            }

            @Override
            public void onFailure(Call<LibraryModel> call, Throwable t) {

            }
        });

    }

    public ArrayList<Model> GetFiles() {
        File downloadsFolder = new File("/storage/emulated/0/myAudioBook/audioBooks/temp/recording/");

        ArrayList<Model> Myfiles = new ArrayList<Model>();

        Model modelList;
        if (!downloadsFolder.exists()) {
            downloadsFolder.mkdir();
        }
        //GET ALL FILES IN DOWNLOAD FOLDER
        File[] files = downloadsFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.getPath().endsWith(".mp3")) {
                modelList = new Model();
                modelList.setName(file.getName());
                modelList.setPath(file.getAbsolutePath());

                Myfiles.add(modelList);
            }

        }
        return Myfiles;
    }

    void stopBGMusic() {

        if (recyclerAdapter.mediaPlayer != null) {

            recyclerAdapter.mediaPlayer.stop();
            recyclerAdapter.mediaPlayer.release();
            recyclerAdapter.mediaPlayer = null;
        }

    }


}
