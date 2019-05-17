package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.doozycod.childrenaudiobook.Adapter.RecyclerAdapter;
import com.doozycod.childrenaudiobook.Helper.Model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Service.Config;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class LibraryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] book_name = {"Amelia Bebelia", "Cat in The Hat", "Drive the Bus", "Frog and Toad", "Go, Dog. Go"};
    Dialog myDialog;
    ImageView login_dialog, login_btn_main, home_btn;
    private MediaPlayer mediaPlayer;
    RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        myDialog = new Dialog(this);
        recyclerView = findViewById(R.id.recycler_view_lib);
        recyclerView.setHasFixedSize(true);
        home_btn = findViewById(R.id.home_btn_lib);
        login_btn_main = findViewById(R.id.login_btn_lib);
        recyclerAdapter = new RecyclerAdapter(this, GetFiles());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter(this, GetFiles()));
        login_btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void ShowPopup(View v) {

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

        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
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


    @Override
    public void onBackPressed() {

        stopBGMusic();

        super.onBackPressed();
    }
}
