package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class ProfileActivity extends AppCompatActivity {
    Dialog myDialog;
    ImageView change_submit_btn, logout_btn;
    EditText et_first_name, et_last_name, et_email, et_phone_no;
    String shared_email, shared_phoneno, shared_firstname, shared_lastname;
    SharedPreferenceMethod sharedPreferenceMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDialog = new Dialog(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        setContentView(R.layout.activity_profile);
        SharedPreferences sp = this.getSharedPreferences("children", Context.MODE_PRIVATE);
        et_first_name = findViewById(R.id.et_first_name);
        logout_btn = findViewById(R.id.logout_btn);
        et_last_name = findViewById(R.id.et_last_name);
        et_email = findViewById(R.id.et_email);
        et_phone_no = findViewById(R.id.et_phone_no);
        shared_email = sp.getString("email", "");
        shared_firstname = sp.getString("firstname", "");
        shared_lastname = sp.getString("lastname", "");
        shared_phoneno = sp.getString("spphone", "");
        et_email.setText(shared_email);
        et_phone_no.setText(shared_phoneno);
        et_first_name.setText(shared_firstname);
        et_last_name.setText(shared_lastname);

        change_submit_btn = findViewById(R.id.change_password_btn_act);
        change_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferenceMethod.Logout();
                Intent intent = new Intent(ProfileActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

                myDialog.dismiss();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(v.getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }
}
