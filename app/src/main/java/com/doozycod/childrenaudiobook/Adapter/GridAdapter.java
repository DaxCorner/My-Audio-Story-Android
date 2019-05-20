package com.doozycod.childrenaudiobook.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.doozycod.childrenaudiobook.Activities.APIService;
import com.doozycod.childrenaudiobook.Activities.BookDetailActivity;
import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class GridAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    APIService apiService;
    List<String> book_image = new ArrayList<String>();
    List<String> book_name = new ArrayList<String>();
    List<String> book_audio_file = new ArrayList<String>();

    public GridAdapter(ChooseYourBookActivity chooseYourBookActivity, APIService apiService, List<String> book_image, List<String> book_name, List<String> book_audio_file) {
        context = chooseYourBookActivity;
        this.apiService = apiService;
        this.book_image = book_image;
        this.book_name = book_name;
        this.book_audio_file = book_audio_file;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return book_image.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        View view = inflater.inflate(R.layout.grid_list_view, null);
        holder.audioBookImage = view.findViewById(R.id.imageview);

        Glide.with(view.getContext()).load("http://" + book_image.get(position)).into(holder.audioBookImage);

        holder.relativeLayout = view.findViewById(R.id.relativelayout);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("audio_file", book_audio_file.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class Holder {
        ImageView audioBookImage;
        RelativeLayout relativeLayout;
    }
}
