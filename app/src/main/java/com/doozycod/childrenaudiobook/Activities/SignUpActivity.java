package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.doozycod.childrenaudiobook.Models.Signup_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

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

    //init volley
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://www.doozycod.in/books-manager/api/User/sign_up.php";
APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        apiService = ApiUtils.getAPIService();

        myDialog = new Dialog(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
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
                signUpRequest(entered_fname,entered_lname,entered_email,entered_password,entered_mobile);

            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        library_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ChooseYourBookActivity.class));

            }
        });


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


//        if (!matcher.matches()) {
//
//            Toast.makeText(getApplicationContext(), "Please Enter A  Valid Email" + email, Toast.LENGTH_LONG).show();
//            return;
//
//        }
        if (!et_pass.getText().toString().equals(et_confirmpass.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "password did not matched!", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }
    }



    public void ShowPopup(View v) {


        myDialog.setContentView(R.layout.custom_login_popup);

        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);
        EditText et_login_dialog = myDialog.findViewById(R.id.et_login_dialog);
        EditText et_password_dialog = myDialog.findViewById(R.id.et_password_dialog);


        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("email password", et_login_dialog.getText().toString() + "  " + et_password_dialog.getText().toString());


                if (et_login_dialog.getText().toString().equals("") || et_password_dialog.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Username and password can't be emapty!", Toast.LENGTH_SHORT).show();
                } else {
//                    loginUser(et_login_dialog.getText().toString(), et_password_dialog.getText().toString());
                }
//                    myDialog.dismiss();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }




    public void signUpRequest(String entered_fname, String entered_lname, String entered_email, String entered_password, String entered_mobile) {
        apiService.signUp(entered_fname, entered_lname, entered_email, entered_password, entered_mobile).enqueue(new Callback<Signup_model>() {

            @Override
            public void onResponse(Call<Signup_model> call, retrofit2.Response<Signup_model> response) {

                if(response.isSuccessful()){
//                    Toast.makeText(SignUpActivity.this, "sucess=>"+response.body(), Toast.LENGTH_LONG).show();
                    Log.e("response=>",response.body().toString()+"");
                    Toast.makeText(SignUpActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(SignUpActivity.this, "error=>"+response.toString(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Signup_model> call, Throwable t) {
                Log.e("API call => ", "Unable to submit post to API.");
            }
        });
    }


}
