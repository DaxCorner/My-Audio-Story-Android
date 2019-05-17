package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.API;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class SignUpActivity extends AppCompatActivity {
    Button sign_up_user;
    ImageView login_button, library_buton, home_button;
    EditText et_firstname, et_lastname, et_email, et_pass, et_confirmpass;
    Dialog myDialog;
    ImageView login_dialog;
    SharedPreferenceMethod sharedPreferenceMethod;
    TextView first_name;
    TextView lastname;
    TextView emailtxt;
    TextView passwordtxt;
    TextView retypepass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        first_name = (TextView) findViewById(R.id.firstname);
        lastname = (TextView) findViewById(R.id.lastname);
        emailtxt = (TextView) findViewById(R.id.emailtext);
        passwordtxt = (TextView) findViewById(R.id.passwordtxt);
        retypepass = (TextView) findViewById(R.id.retypepass);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");


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


                signUpUser();


//                  startActivity(new Intent(SignUpActivity.this, SaveShareYourStoryActivity.class));
//                finish();
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

    private void signUpUser() {


        final String user_email = et_email.getText().toString().trim();
        final String fname = et_firstname.getText().toString().trim();
        final String lname = et_lastname.getText().toString().trim();
        final String user_password = et_pass.getText().toString().trim();


        Log.e("Sign Up Details", user_email + fname + lname + user_password);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SIGN_UP_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response ---->", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {

            protected Map<String, String> getParams() {
                Map<String, String> parm = new HashMap<>();

                parm.put("email", user_email);
                parm.put("password", user_password);
                parm.put("first_name", fname);
                parm.put("last_name", lname);
                parm.put("mobile_number", "");

                return parm;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginUser(String email_id, String password) {

        Log.e("Email and Password", email_id + " --- " + password);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doozycod.in/books-manager/api/User/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    String id = jsonObject.getString("id");
                    String email = jsonObject.getString("email");
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    String mobile_number = jsonObject.getString("mobile_number");


                    Log.e("Login Message Response", jsonObject + "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error :=", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<>();

                param.put("email", email_id);
                param.put("password", password);

                return param;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                    loginUser(et_login_dialog.getText().toString(), et_password_dialog.getText().toString());
                }
//                    myDialog.dismiss();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }


}
