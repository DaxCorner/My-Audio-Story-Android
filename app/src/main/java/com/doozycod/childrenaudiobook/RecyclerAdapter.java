package com.doozycod.childrenaudiobook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.doozycod.childrenaudiobook.R.drawable.dark_dot;
import static com.doozycod.childrenaudiobook.R.drawable.dark_line;
import static com.doozycod.childrenaudiobook.R.drawable.light_line;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    Context c;
    String[] book_name;

    public RecyclerAdapter(Context c, String[] book_name) {
        this.c = c;
        this.book_name = book_name;
    }

    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_view, viewGroup, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int i) {

        holder.book_name_txt.setText(book_name[i]);
        if (i % 2 == 1) {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(light_line));
        } else {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(dark_line));

        }
    }

    @Override
    public int getItemCount() {
        return book_name.length;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView book_name_txt;
        RelativeLayout relativeLayout;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            book_name_txt = itemView.findViewById(R.id.story_name_txt);
            relativeLayout = itemView.findViewById(R.id.recycler_layout);
        }
    }
}
