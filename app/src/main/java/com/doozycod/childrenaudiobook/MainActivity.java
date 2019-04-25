package com.doozycod.childrenaudiobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    public static int[] grid_image = {R.drawable.img1, R.drawable.img2,  R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6};

    GridView gridView;
    GridAdapter gridAdapter;
    AlertDialog alertDialog;
    LinearLayout login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridview);
        login_btn = findViewById(R.id.login_btnmain);
        gridAdapter = new GridAdapter(this, grid_image);
        gridView.setAdapter(gridAdapter);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();

            }
        });

    }

}
