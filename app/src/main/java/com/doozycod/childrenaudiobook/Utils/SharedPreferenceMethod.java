package com.doozycod.childrenaudiobook.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ooo on 6/22/2018.
 */

public class SharedPreferenceMethod {
    Context context;
    public static String gemail, gpassword;

    public SharedPreferenceMethod(Context context) {
        this.context = context;
    }


    public void spInsert(String semail, String spassword) {
        SharedPreferences sp = context.getSharedPreferences("children", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.putString("email", semail);
        sp_editior.putString("password", spassword);
        sp_editior.commit();

    }

    public String getData() {
        SharedPreferences sp = context.getSharedPreferences("children", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        gemail = sp.getString("email", "");
        gpassword = sp.getString("password", "");
        return gemail;
    }

    public boolean checkLogin() {
        SharedPreferences sp = context.getSharedPreferences("children", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        boolean cemail = sp.getString("email", "").isEmpty();
        boolean cpassword = sp.getString("password", "").isEmpty();

        return cemail || cpassword;
    }

    public void Logout() {
        SharedPreferences sp = context.getSharedPreferences("children", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();


        sp_editior.clear().commit();
    }
}
