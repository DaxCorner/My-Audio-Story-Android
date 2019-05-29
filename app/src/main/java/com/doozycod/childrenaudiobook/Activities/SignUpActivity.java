package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.Signup_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
import com.hmomeni.progresscircula.ProgressCircula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class SignUpActivity extends AppCompatActivity {
    Button sign_up_user;
    ImageView login_button, library_buton, home_button;
    EditText et_firstname, et_lastname, et_email, et_pass, et_confirmpass, et_phone;
    Dialog myDialog;
    ImageView login_dialog;
    SharedPreferenceMethod sharedPreferenceMethod;
    TextView first_name;
    TextView lastname;
    TextView emailtxt;
    TextView passwordtxt;
    TextView retypepass, phone_txt;
    String entered_email, entered_fname, entered_lname, entered_password, entered_mobile;
    CustomProgressBar progressBar;
    //init volley
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://www.doozycod.in/books-manager/api/User/sign_up.php";
    APIService apiService;
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        apiService = ApiUtils.getAPIService();
        progressBar = new CustomProgressBar(this);
        myDialog = new Dialog(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        sign_up_user = findViewById(R.id.sign_upactivity_btn);
        home_button = findViewById(R.id.home_btn_signup);
        library_buton = findViewById(R.id.lib_btn_on_signup);
        et_firstname = findViewById(R.id.first_name);
        et_lastname = findViewById(R.id.last_name);
        et_email = findViewById(R.id.email_phone);
        et_pass = findViewById(R.id.password);
        et_confirmpass = findViewById(R.id.confirm_pass);
        et_phone = findViewById(R.id.et_phone);

        first_name = (TextView) findViewById(R.id.firstname);
        lastname = (TextView) findViewById(R.id.lastname);
        emailtxt = (TextView) findViewById(R.id.emailtext);
        passwordtxt = (TextView) findViewById(R.id.passwordtxt);
        retypepass = (TextView) findViewById(R.id.retypepass);
        phone_txt = findViewById(R.id.phone_no_txt);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        phone_txt.setTypeface(custom_font);
        first_name.setTypeface(custom_font);
        lastname.setTypeface(custom_font);
        emailtxt.setTypeface(custom_font);
        passwordtxt.setTypeface(custom_font);
        retypepass.setTypeface(custom_font);
        login_button = findViewById(R.id.login_btn_signup);

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        sign_up_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                entered_email = et_email.getText().toString().trim();
                entered_fname = et_firstname.getText().toString().trim();
                entered_lname = et_lastname.getText().toString().trim();
                entered_password = et_pass.getText().toString().trim();
                entered_mobile = et_phone.getText().toString().trim();
                SignUpValidation();
            }
        });
        if (sharedPreferenceMethod != null) {
            if (sharedPreferenceMethod.checkLogin()) {
//                ShowProgressDialog();

                login_button.setImageResource(R.drawable.login_btn_pressed);

                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            } else {
//                ShowProgressDialog();
                login_button.setImageResource(R.drawable.profile_btn_pressed);
                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                    }
                });

            }
        }
        library_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ChooseYourBookActivity.class));

            }
        });


    }

    public void ShowProgressDialog() {
        progressBar.showProgress();
    }

    public void HideProgressDialog() {
        progressBar.hideProgress();

    }

    private void SignUpValidation() {
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";
        String email = et_email.getText().toString();

        Matcher matcher = Pattern.compile(validemail).matcher(email);

        if (TextUtils.isEmpty(et_firstname.getText().toString())) {
            Toast.makeText(this, "First Name can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_lastname.getText().toString())) {
            Toast.makeText(this, "Last Name can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matcher.matches()) {

            Toast.makeText(getApplicationContext(), "Please Enter A  Valid Email" + email, Toast.LENGTH_LONG).show();
            return;

        }
        if (TextUtils.isEmpty(et_phone.getText().toString())) {
            Toast.makeText(this, "Enter phone no.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_pass.getText().toString())) {
            Toast.makeText(this, "Enter your password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_pass.getText().toString().length() <= 6) {
            Toast.makeText(this, "password must be 6 letters or above", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!et_pass.getText().toString().equals(et_confirmpass.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "password did not matched!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().length() <= 7) {
            Toast.makeText(SignUpActivity.this, "Mobile Number Should Be of At least 8 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpRequest(entered_fname, entered_lname, entered_email, entered_password, entered_mobile, android_id);
        ShowProgressDialog();

    }


    public void ShowPopup() {


        myDialog.setContentView(R.layout.custom_login_popup);

        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);
        EditText et_login_dialog = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_dialog = myDialog.findViewById(R.id.et_password_dialog);


        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("email password", et_login_dialog.getText().toString() + "  " + et_password_dialog.getText().toString());


                if (et_login_dialog.getText().toString().equals("") || et_password_dialog.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Username and password can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String login_email = et_login_dialog.getText().toString();
                    String login_password = et_password_dialog.getText().toString();
                    loginRequest(login_email, login_password);
                    ShowProgressDialog();
                }
//                    myDialog.dismiss();

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
                        login_button.setImageResource(R.drawable.profile_btn_pressed);
                        if (!sharedPreferenceMethod.checkLogin()) {
                            login_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                                }
                            });
                            myDialog.dismiss();
                        } else {
                            ShowPopup();
                        }

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

    public void signUpRequest(String entered_fname, String entered_lname, String entered_email, String entered_password, String entered_mobile, String device_id) {
        apiService.signUp(entered_fname, entered_lname, entered_email, entered_password, entered_mobile, device_id).enqueue(new Callback<Login_model>() {

            @Override
            public void onResponse(Call<Login_model> call, retrofit2.Response<Login_model> response) {
                HideProgressDialog();
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.body().getStatus()
                            + response.body().getMessage()
                            + response.body().getUser_id()

                            + response.body().getMobile_number(), Toast.LENGTH_SHORT).show();
                    sharedPreferenceMethod.spInsert(response.body().getEmail(), entered_password, response.body().getFirst_name(), response.body().getLast_name(), response.body().getMobile_number(), response.body().getUser_id());
                }


            }

            @Override
            public void onFailure(Call<Login_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");
            }
        });
    }


}
