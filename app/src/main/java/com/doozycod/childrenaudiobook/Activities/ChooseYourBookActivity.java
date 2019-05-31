package com.doozycod.childrenaudiobook.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.doozycod.childrenaudiobook.Adapter.Login_ViewPagerAdapter;
import com.doozycod.childrenaudiobook.Adapter.ViewPagerAdapter;

import com.doozycod.childrenaudiobook.Models.BooksModel_login;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Service.MyFirebaseMessagingService;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hmomeni.progresscircula.ProgressCircula;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class ChooseYourBookActivity extends AppCompatActivity {

    SharedPreferenceMethod sharedPreferenceMethod;

    View view;
    ImageView login_btn, home_btn, library_btn, popup_login, popup_signup, login_dialog;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Dialog myDialog;
    Boolean isPressed = true;
    APIService apiService;
    List<BooksModel_login.book_detail> book_list_login = null;
    List<Books_model.book_detail> book_list = null;
    String android_id;
    String book_id;
    CustomProgressBar progressDialog;
    LayoutInflater inflater;
    RelativeLayout v0;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        progressDialog = new CustomProgressBar(this);

        login_btn = findViewById(R.id.login_btn_main);
        inflater = this.getLayoutInflater();
        v0 = (RelativeLayout) inflater.inflate(R.layout.view_pager_layout, null);
        if (generatePushToken() == null) {
            generatePushToken();
        }
        //      Checking that run is first time of the app or not
//        Boolean isFirstRun = getSharedPreferences("children", MODE_PRIVATE)
//                .getBoolean("isFirstRun", true);
//
////      Condition for Checking isFirstRun or not
//        if (isFirstRun) {
//            startActivity(new Intent(ChooseYourBookActivity.this, SplashActivity.class));
//        }
//        getSharedPreferences("children", MODE_PRIVATE).edit()
//                .putBoolean("isFirstRun", false).apply();


        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("Device_id", android_id);

        Log.e("Token", sharedPreferenceMethod.getToken());
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });
        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
                ShowProgressDialog();
                fetchBookDataAndViewPager();
                login_btn.setImageResource(R.drawable.login_btn_pressed);

                login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            } else {
                ShowProgressDialog();
                fetchBookDataAndLoginViewPager();
                login_btn.setImageResource(R.drawable.profile_btn_pressed);
                login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ChooseYourBookActivity.this, ProfileActivity.class));
                    }
                });

            }
        }
        apiService = ApiUtils.getAPIService();
        home_btn = findViewById(R.id.home_btn);
        library_btn = findViewById(R.id.lib_btn_main);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        view = new View(this);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChooseYourBookActivity.this, RecordYourOwnActivity.class));
            }
        });


        library_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseYourBookActivity.this, LibraryActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (isPressed) {
            ShowPopupRating(view);
            isPressed = false;

        } else {
            isPressed = false;
            finish();
        }

    }

    public String generatePushToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        token = task.getResult().getToken();
                        sharedPreferenceMethod.spSaveToken(token);
                        // Log and toast

                        Log.e("TOKEN", token);
                    }
                });
        return token;
    }

    public void ShowPopup() {

        myDialog.setContentView(R.layout.custom_popup);
        popup_login = myDialog.findViewById(R.id.select_login);
        popup_signup = myDialog.findViewById(R.id.sign_upact_btn);
        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(ChooseYourBookActivity.this, SignUpActivity.class));
            }
        });
        popup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                showLoginPopUp();

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();

    }

    public void showLoginPopUp() {

        myDialog.setContentView(R.layout.custom_login_popup);

        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);

        EditText et_email_btn = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_btn = myDialog.findViewById(R.id.et_password_dialog);
        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_btn.getText().toString().equals("") || et_password_btn.getText().toString().equals("")) {
                    Toast.makeText(ChooseYourBookActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
                } else {
                    String pass = et_password_btn.getText().toString();
                    if (pass.length() > 6) {
                        String login_email = et_email_btn.getText().toString();
                        String login_password = et_password_btn.getText().toString();
                        if (generatePushToken() != null) {
                            ShowProgressDialog();
                            loginRequest(login_email, login_password, generatePushToken());
                        }

                    } else {
                        Toast.makeText(ChooseYourBookActivity.this, "Password is at least 7 words!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    public void loginRequest(String entered_email, String entered_password, String token) {
        apiService.signIn(entered_email, entered_password, token, android_id).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {

                Log.e("signIn Response", response.body().getStatus());
                HideProgressDialog();

                if (response.body().getStatus().equals("true")) {

//                    fetchBookDataAndLoginViewPager();
                    startActivity(new Intent(ChooseYourBookActivity.this, ChooseYourBookActivity.class));
                    finish();

                    sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
//                    sharedPreferenceMethod.saveLogin(true);
                    Log.e("Login Details", response.body().getStatus() + "  " + response.body().getEmail() + "  " + response.body().getFirst_name() + "  " + response.body().getLast_name() + "  " + response.body().getMobile_number() + "\n userID  " + response.body().getUser_id());
                    myDialog.dismiss();
                    login_btn.setImageResource(R.drawable.profile_btn_pressed);
                    Log.e("Shared CheckLogin", sharedPreferenceMethod.checkLogin() + "");

                } else {
                    errorDialogLogin();
                }

            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");
                HideProgressDialog();

            }

        });
    }

    public void errorDialogLogin() {

        Dialog errorDialog = new Dialog(ChooseYourBookActivity.this);
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

    public void ShowPopupRating(View v) {

        myDialog.setContentView(R.layout.custom_popup_rate_our_app);
        ImageView submit_btn, later_btn;
        submit_btn = myDialog.findViewById(R.id.rating_now_btn);
        later_btn = myDialog.findViewById(R.id.later_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        later_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();

    }

    public void ShowProgressDialog() {
        progressDialog.showProgress();
    }

    public void HideProgressDialog() {
        progressDialog.hideProgress();

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(ChooseYourBookActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(ChooseYourBookActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(ChooseYourBookActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (ChooseYourBookActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (ChooseYourBookActivity.this, Manifest.permission.CAMERA)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            200);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            200);
                }
            }
        } else {
            // write your logic code if permission already granted
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 200:
                if (grantResults.length > 0) {
                    boolean RecordAudio = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (RecordAudio && readExternalFile && writeExternalFile) {
                        // write your logic here

                    } else {

                        requestPermissions(
                                new String[]{Manifest.permission
                                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                200);

                    }
                }
                break;
        }
    }


    public void fetchBookDataAndLoginViewPager() {
        apiService = ApiUtils.getAPIService();


        apiService.getAllBooks_login(sharedPreferenceMethod.getUserId()).enqueue(new Callback<BooksModel_login>() {
            @Override
            public void onResponse(Call<BooksModel_login> call, Response<BooksModel_login> response) {

                HideProgressDialog();

                for (int index = 0; index < response.body().getBook_list_data().size(); index++) {

                    book_list_login = response.body().getBook_list_data();

                }
//                viewPagerAdapter = new ViewPagerAdapter(ChooseYourBookActivity.this, apiService, book_image_list, book_name_list, book_audio_file_list);

                Login_ViewPagerAdapter loginViewPagerAdapter = new Login_ViewPagerAdapter(ChooseYourBookActivity.this, book_list_login, sharedPreferenceMethod);
                loginViewPagerAdapter.addView(v0, 0);
                loginViewPagerAdapter.notifyDataSetChanged();
                viewPager = findViewById(R.id.photos_viewpager);

                viewPager.setAdapter(loginViewPagerAdapter);


                dotscount = loginViewPagerAdapter.getCount();
                dots = new ImageView[dotscount];


                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(ChooseYourBookActivity.this);

                    if (dotscount > 1) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(10, 0, 10, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                if (dotscount > 1) {
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onFailure(Call<BooksModel_login> call, Throwable t) {
                HideProgressDialog();
            }

        });


    }

    public void fetchBookDataAndViewPager() {
        apiService = ApiUtils.getAPIService();


        apiService.getAllBooks().enqueue(new Callback<Books_model>() {
            @Override
            public void onResponse(Call<Books_model> call, Response<Books_model> response) {
                HideProgressDialog();

                for (int index = 0; index < response.body().getBook_list_data().size(); index++) {

                    book_list = response.body().getBook_list_data();
                    Log.e("Book Image", book_list.get(0).getBook_image());
                }
//                viewPagerAdapter = new ViewPagerAdapter(ChooseYourBookActivity.this, apiService, book_image_list, book_name_list, book_audio_file_list);

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ChooseYourBookActivity.this, book_list, sharedPreferenceMethod);
                viewPager = findViewById(R.id.photos_viewpager);
                viewPagerAdapter.addView(v0, 0);
                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setAdapter(viewPagerAdapter);


                dotscount = viewPagerAdapter.getCount();


                dots = new ImageView[dotscount];


                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(ChooseYourBookActivity.this);


                    if (dotscount > 1) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(10, 0, 10, 0);

                    sliderDotspanel.addView(dots[i], params);

                }


                if (dotscount > 1) {
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Books_model> call, Throwable t) {
                HideProgressDialog();
            }


        });


    }


}




