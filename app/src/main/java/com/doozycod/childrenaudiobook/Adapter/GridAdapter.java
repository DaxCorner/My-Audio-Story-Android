package com.doozycod.childrenaudiobook.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doozycod.childrenaudiobook.Activities.BookDetailActivity;
import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class GridAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    int[] grid_ico;

    public GridAdapter(ChooseYourBookActivity chooseYourBookActivity, int[] grid_icon) {
        context = chooseYourBookActivity;

        grid_ico = grid_icon;
//        this.url = url;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return grid_ico.length;
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
        holder.audioBookImage.setBackgroundResource(grid_ico[position]);
        holder.relativeLayout = view.findViewById(R.id.relativelayout);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, BookDetailActivity.class));
            }
        });
        return view;
    }

    public class Holder {
        ImageView audioBookImage;
        RelativeLayout relativeLayout;
    }
}
