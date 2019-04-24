package com.doozycod.childrenaudiobook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GridAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    int[] grid_ico;

    public GridAdapter(MainActivity mainActivity, int[] grid_icon) {
        context = mainActivity;

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
        holder.linearLayout = view.findViewById(R.id.layout);
        holder.audioBookImage.setBackgroundResource(grid_ico[position]);

        return view;
    }

    public class Holder {
        ImageView audioBookImage;
        LinearLayout linearLayout;
    }
}
