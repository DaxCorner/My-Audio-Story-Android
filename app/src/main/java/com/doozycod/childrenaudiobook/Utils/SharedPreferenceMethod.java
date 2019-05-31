package com.doozycod.childrenaudiobook.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.doozycod.childrenaudiobook.Models.SharedModel;

/**
 * Created by ooo on 6/22/2018.
 */

public class SharedPreferenceMethod {
    Context context;
    public static String spemail, sppassword, spfirst_name, splast_name, spMobileNumber, spUser_id, spBook_id, spBook_name, sp_token;

    public SharedPreferenceMethod(Context context) {
        this.context = context;
    }


    public void spInsert(String semail, String spassword, String spfirst_name, String splast_name, String spMobileNumber, String spUser_id) {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putString("spUser_id", spUser_id);
        sp_editior.putString("email", semail);
        sp_editior.putString("password", spassword);
        sp_editior.putString("firstname", spfirst_name);
        sp_editior.putString("lastname", splast_name);
        sp_editior.putString("spphone", spMobileNumber);
        sp_editior.commit();

    }

    public void spSaveToken(String token) {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putString("push_token", token);
        sp_editior.commit();

    }

    public String getToken() {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();

        SharedModel sharedModel = new SharedModel();
        sp_token = sp.getString("push_token", "");
        sharedModel.setToken(sp_token);
        return sp_token;
    }

    public void saveLogin(boolean login) {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putBoolean("login", login);
        sp_editior.commit();

    }

    public void login(String login) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editor = sharedPreferences.edit();
        sp_editor.putString("loginEmail", login);
        sp_editor.apply();
    }


    public String getUserId() {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();

        SharedModel sharedModel = new SharedModel();
        spUser_id = sp.getString("spUser_id", "");
        sharedModel.setUser_id(spUser_id);
        return spUser_id;
    }


    public boolean checkLogin() {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();

        boolean email = sp.getString("email", "").isEmpty();
        boolean password = sp.getString("password", "").isEmpty();
        return email || password;
    }

    public void Logout() {
        SharedPreferences sp = context.getSharedPreferences("audiobook", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();

        sp_editior.clear().apply();

    }
}
