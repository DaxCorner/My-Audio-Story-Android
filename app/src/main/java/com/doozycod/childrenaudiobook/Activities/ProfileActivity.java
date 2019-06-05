package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.Models.updateProfileModel;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class ProfileActivity extends AppCompatActivity {
    Dialog myDialog;
    ImageView change_submit_btn, change_pass_btn, logout_btn, home_btn_profile, lib_btn_profile, login_profile, update_btn;
    EditText et_first_name, et_last_name, et_email, et_phone_no;
    String shared_email, shared_phoneno, shared_firstname, shared_lastname;
    SharedPreferenceMethod sharedPreferenceMethod;
    APIService apiService;
    CustomProgressBar progressBar;
    String android_id;
    String firstname, lastname, email, phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDialog = new Dialog(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_profile);
        SharedPreferences sp = this.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        home_btn_profile = findViewById(R.id.home_btn_profile);
        lib_btn_profile = findViewById(R.id.lib_btn_profile);
        login_profile = findViewById(R.id.profile_btn);
        et_first_name = findViewById(R.id.et_first_name);
        logout_btn = findViewById(R.id.logout_btn);
        et_last_name = findViewById(R.id.et_last_name);
        et_email = findViewById(R.id.et_email);
        et_phone_no = findViewById(R.id.et_phone_no);
        update_btn = findViewById(R.id.update_profile_btn);


        shared_email = sp.getString("email", "");
        shared_firstname = sp.getString("firstname", "");
        shared_lastname = sp.getString("lastname", "");
        shared_phoneno = sp.getString("spphone", "");
        et_email.setText(shared_email);
        et_phone_no.setText(shared_phoneno);
        et_first_name.setText(shared_firstname);
        et_last_name.setText(shared_lastname);

        home_btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        lib_btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LibraryActivity.class));
            }
        });
        change_pass_btn = findViewById(R.id.change_password_btn_act);
        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup(v);
            }
        });
        et_phone_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == et_phone_no) {
                    // is only executed if the EditText was directly changed by the user
                    shared_phoneno = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == et_phone_no) {
                    // is only executed if the EditText was directly changed by the user
                    shared_email = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_first_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == et_first_name) {
                    // is only executed if the EditText was directly changed by the user
                    shared_firstname = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_last_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == et_last_name) {
                    // is only executed if the EditText was directly changed by the user
                    shared_lastname = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgressDialog();

                Log.e("String Email", shared_phoneno);
                updateProfile(sharedPreferenceMethod.getUserId(), et_email.getText().toString(), shared_lastname, shared_email, shared_phoneno);


                SharedPreferences sp = ProfileActivity.this.getSharedPreferences("audiobook", MODE_PRIVATE);
                sharedPreferenceMethod.spInsert(shared_email, sp.getString("password", ""), shared_firstname, shared_lastname, shared_phoneno, sharedPreferenceMethod.getUserId());


            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sharedPreferenceMethod.saveLogin(false);
                ShowProgressDialog();
                logout(sharedPreferenceMethod.getUserId(),sharedPreferenceMethod.getToken());

            }
        });
    }


    public void ShowPopup(View v) {


        myDialog.setContentView(R.layout.custom_change_password_pop_up);

        change_submit_btn = myDialog.findViewById(R.id.change_pass_submit_btn);
        EditText et_current_pass = myDialog.findViewById(R.id.et_current_password);
        EditText et_password = myDialog.findViewById(R.id.et_new_password);
        EditText et_confirm_password = myDialog.findViewById(R.id.et_confirm_password);


        change_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_confirm_password.getText().toString().equals(et_password.getText().toString())) {
                    ShowProgressDialog();
                    changePassword(sharedPreferenceMethod.getUserId(), et_current_pass.getText().toString(), et_password.getText().toString());
                } else {
                    Toast.makeText(ProfileActivity.this, "check your password!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(v.getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    public void updateProfile(String user_id, String first_name, String last_name, String email, String mobile_number) {
        apiService.updateProfile(user_id, first_name, last_name, email, mobile_number).
                enqueue(new Callback<updateProfileModel>() {

                    @Override
                    public void onResponse(Call<updateProfileModel> call, retrofit2.Response<updateProfileModel> response) {
                        HideProgressDialog();
                        if (response.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), response.body().getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<updateProfileModel> call, Throwable t) {
                        Log.e("API call => ", "Unable to submit post to API.");
                        HideProgressDialog();
                    }
                });

    }

    public void ShowProgressDialog() {
        progressBar.showProgress();
    }

    public void HideProgressDialog() {
        progressBar.hideProgress();

    }

    public void changePassword(String user_id, String old_password, String new_password) {
        apiService.changePassword(user_id, old_password, new_password).
                enqueue(new Callback<ResultObject>() {

                    @Override
                    public void onResponse(Call<ResultObject> call, retrofit2.Response<ResultObject> response) {
                        HideProgressDialog();
                        if (response.isSuccessful()) {


                            if (response.body().getSuccess().equals("true")) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage()
                                        , Toast.LENGTH_SHORT).show();
                                myDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().getMessage()
                                        , Toast.LENGTH_SHORT).show();
                            }

                        }


                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.e("API call => ", "Unable to submit post to API.");
                    }
                });

    }

    public void logout(String user_id, String existing_token) {
        apiService.logout(user_id, existing_token).
                enqueue(new Callback<ResultObject>() {

                    @Override
                    public void onResponse(Call<ResultObject> call, retrofit2.Response<ResultObject> response) {
                        HideProgressDialog();
                        if (response.isSuccessful()) {
                            sharedPreferenceMethod.spSaveToken("");
                            Toast.makeText(getApplicationContext(), response.body().getMessage()
                                    , Toast.LENGTH_SHORT).show();
                            if (response.body().getSuccess().equals("true")) {
                                sharedPreferenceMethod.Logout();
                                Intent intent = new Intent(ProfileActivity.this, ChooseYourBookActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.e("API call => ", "Unable to submit post to API.");
                        HideProgressDialog();
                    }
                });

    }
}
