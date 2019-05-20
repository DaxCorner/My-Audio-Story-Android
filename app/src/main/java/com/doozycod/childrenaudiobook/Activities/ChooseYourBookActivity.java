package com.doozycod.childrenaudiobook.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.doozycod.childrenaudiobook.Adapter.ViewPagerAdapter;

import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class ChooseYourBookActivity extends AppCompatActivity {
    public static int[] grid_image = {R.drawable.book_01, R.drawable.book_02, R.drawable.book_03, R.drawable.book_04};
    SharedPreferenceMethod sharedPreferenceMethod;
    List<String> book_image = new ArrayList<String>();
    List<String> book_name = new ArrayList<String>();
    List<String> book_audio_file = new ArrayList<String>();
    List<String> book_id = new ArrayList<String>();
    List<String> book_content_file = new ArrayList<String>();
    View view;
    ImageView login_btn, home_btn, library_btn, popup_login, popup_signup, login_dialog;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Dialog myDialog;
    Boolean isPressed = true;
    APIService apiService;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_main);

        if(sharedPreferenceMethod.checkLogin())
        {   // condition true means user is already login
//            Intent i = new Intent(this,);
//            startActivityForResult(i, 1);
        }

        else
        {
            // condition false take it user on login form
        }
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();
        home_btn = findViewById(R.id.home_btn);
        library_btn = findViewById(R.id.lib_btn_main);
        myDialog = new Dialog(this);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        view = new View(this);
        login_btn = findViewById(R.id.login_btn_main);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChooseYourBookActivity.this, RecordYourOwnActivity.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });

        library_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseYourBookActivity.this, LibraryActivity.class));
            }
        });

        fetchBookDataAndViewPager();

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


    public void ShowPopup(View v) {

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
                    Toast.makeText(ChooseYourBookActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
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
        apiService.signIn(entered_email, entered_password).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus() == "true") {
                        sharedPreferenceMethod.spInsert(entered_email, entered_password);
                    }
                    Toast.makeText(getApplicationContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");

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


    public void fetchBookDataAndViewPager() {
        apiService = ApiUtils.getAPIService();
        apiService.getAllBooks().enqueue(new Callback<Books_model>() {
            @Override
            public void onResponse(Call<Books_model> call, Response<Books_model> response) {
                for (int index = 0; index < response.body().getBook_list_data().size(); index++) {

                    book_name.add(response.body().getBook_list_data().get(index).getBook_name());
                    book_image.add(response.body().getBook_list_data().get(index).getBook_image());
                    book_audio_file.add(response.body().getBook_list_data().get(index).getBook_audio_file());
                    book_content_file.add(response.body().getBook_list_data().get(index).getBook_content_file());
                    book_id.add(response.body().getBook_list_data().get(index).getBook_id());

                }
                viewPagerAdapter = new ViewPagerAdapter(ChooseYourBookActivity.this, grid_image, apiService, book_image, book_name, book_audio_file);
                viewPager = findViewById(R.id.photos_viewpager);

                viewPager.setAdapter(viewPagerAdapter);


                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(ChooseYourBookActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(10, 0, 10, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));

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

            }
        });


    }
}




