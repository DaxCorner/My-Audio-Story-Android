package com.doozycod.childrenaudiobook.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doozycod.childrenaudiobook.Helper.Model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Activities.ShareYourStoryActivity;

import java.io.File;
import java.util.ArrayList;

import static com.doozycod.childrenaudiobook.R.drawable.dark_line;
import static com.doozycod.childrenaudiobook.R.drawable.light_line;
import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    Context c;
    ImageView delete_yes, delete_no;
    ArrayList<Model> modelArrayList;

    public RecyclerAdapter(Context c, ArrayList<Model> modelArrayList) {
        this.c = c;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_view, viewGroup, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int i) {
        final Model fileModel = (Model) this.modelArrayList.get(i);
        holder.book_name_txt.setText(fileModel.getName());

        if (i % 2 == 1) {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(light_line));
        } else {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(dark_line));

        }
        holder.share_story_recycler_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c, ShareYourStoryActivity.class));
            }
        });
        holder.delete_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(fileModel.getPath(), v, i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView book_name_txt;
        RelativeLayout relativeLayout;
        ImageView delete_story_btn, share_story_recycler_view_btn;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            book_name_txt = itemView.findViewById(R.id.story_name_txt);
            relativeLayout = itemView.findViewById(R.id.recycler_layout);
            delete_story_btn = itemView.findViewById(R.id.delete_story_btn);
            share_story_recycler_view_btn = itemView.findViewById(R.id.share_story_recycler_view_btn);


        }
    }

    public void ShowPopup(String filePath, View v, int i) {

        Dialog myDialog = new Dialog(c);

        myDialog.setContentView(R.layout.custom_delete_popup);
        delete_no = myDialog.findViewById(R.id.delete_no);
        delete_yes = myDialog.findViewById(R.id.delete_yes);

        delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });
        delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File deleteFile = new File(filePath);
                deleteFile.delete();
                modelArrayList.remove(i);
                myDialog.dismiss();
                notifyItemRemoved(i);

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(v.getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }


}
