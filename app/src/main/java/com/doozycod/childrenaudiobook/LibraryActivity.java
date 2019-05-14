package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class LibraryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] book_name = {"Amelia Bebelia", "Cat in The Hat", "Drive the Bus", "Frog and Toad", "Go, Dog. Go"};
    Dialog myDialog;
    ImageView login_dialog, login_btn_main, home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        myDialog = new Dialog(this);
        recyclerView = findViewById(R.id.recycler_view_lib);
        recyclerView.setHasFixedSize(true);
        home_btn = findViewById(R.id.home_btn_lib);
        login_btn_main = findViewById(R.id.login_btn_lib);
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
                startActivity(new Intent(LibraryActivity.this, MainActivity.class));
            }
        });
    }

    public void ShowPopup(View v) {

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
}
