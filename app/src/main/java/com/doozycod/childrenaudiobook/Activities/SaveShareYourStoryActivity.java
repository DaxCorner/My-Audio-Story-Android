package com.doozycod.childrenaudiobook.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.R;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class SaveShareYourStoryActivity extends AppCompatActivity {

    ImageView save_btn, share_btn, homebtn, lib_btn, login_btn, login_dialog;
    Dialog myDialog;
    EditText name_recorded_story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_audio);

        save_btn = findViewById(R.id.save_story_btn);
        share_btn = findViewById(R.id.share_story_btn_on_record);
        login_btn = findViewById(R.id.login_btn_recorded);
        homebtn = findViewById(R.id.home_btn_recorded);

        lib_btn = findViewById(R.id.lib_btn_recorded);
        name_recorded_story = findViewById(R.id.name_recorded_story);
        String name_recorded_et = name_recorded_story.getText().toString();
        myDialog = new Dialog(this);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name_recorded_et.equals("")) {
                    Toast.makeText(SaveShareYourStoryActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SaveShareYourStoryActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(SaveShareYourStoryActivity.this, "Shared!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SaveShareYourStoryActivity.this, ShareYourStoryActivity.class));
            }
        });

        lib_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveShareYourStoryActivity.this, LibraryActivity.class));
            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveShareYourStoryActivity.this, ChooseYourBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);

            }
        });

    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.custom_popup);

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
}
