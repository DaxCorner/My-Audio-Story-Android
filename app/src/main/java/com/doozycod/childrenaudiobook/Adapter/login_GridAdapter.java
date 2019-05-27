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
import com.doozycod.childrenaudiobook.Models.BooksModel_login;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

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

public class login_GridAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    List<BooksModel_login.book_detail> Book_list_data = new ArrayList<>();
    SharedPreferenceMethod sharedPreferenceMethod;

    public login_GridAdapter(ChooseYourBookActivity chooseYourBookActivity, List<BooksModel_login.book_detail> Book_list_data, SharedPreferenceMethod sharedPreferenceMethod) {

        context = chooseYourBookActivity;
        this.sharedPreferenceMethod = sharedPreferenceMethod;
        this.Book_list_data = Book_list_data;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return Book_list_data.size();
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

        Glide.with(view.getContext()).load("http://" + Book_list_data.get(position).getBook_image()).into(holder.audioBookImage);

        holder.relativeLayout = view.findViewById(R.id.relativelayout);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("audio_file", Book_list_data.get(position).getBook_audio_file());
                bundle.putString("book_id", Book_list_data.get(position).getBook_id());
                bundle.putString("is_paid", Book_list_data.get(position).getIs_paid());
                bundle.putString("user_id", sharedPreferenceMethod.getUserId());


                bundle.putString("player_book_img", Book_list_data.get(position).getBook_image());
                bundle.putString("player_book_name", Book_list_data.get(position).getBook_name());

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
