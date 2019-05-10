package com.doozycod.childrenaudiobook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LibraryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] book_name = {"Amelia Bebelia", "Cat in The Hat", "Drive the Bus", "Frog and Toad", "Go, Dog. Go"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        recyclerView = findViewById(R.id.recycler_view_lib);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter(this, book_name));
    }
}
