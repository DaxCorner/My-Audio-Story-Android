package com.doozycod.childrenaudiobook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doozycod.childrenaudiobook.Activities.BookDetailActivity;
import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    List<Books_model.book_detail> Book_list_data = new ArrayList<>();
    SharedPreferenceMethod sharedPreferenceMethod;

    public GridAdapter(ChooseYourBookActivity chooseYourBookActivity, List<Books_model.book_detail> Book_list_data, SharedPreferenceMethod sharedPreferenceMethod) {

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
        Bundle bundle = new Bundle();

        View view = inflater.inflate(R.layout.grid_list_view, null);
        holder.audioBookImage = view.findViewById(R.id.imageview);
        holder.relativeLayout = view.findViewById(R.id.relativelayout);

        Glide.with(view.getContext()).load("http://" + Book_list_data.get(position).getBook_image()).into(holder.audioBookImage);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                bundle.putString("audio_file", Book_list_data.get(position).getBook_audio_file());
                bundle.putString("book_id", Book_list_data.get(position).getBook_id());
                bundle.putString("user_id", sharedPreferenceMethod.getUserId());

                bundle.putString("player_book_img", Book_list_data.get(position).getBook_image());

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
