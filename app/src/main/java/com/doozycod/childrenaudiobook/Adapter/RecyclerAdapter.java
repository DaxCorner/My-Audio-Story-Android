package com.doozycod.childrenaudiobook.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.doozycod.childrenaudiobook.Activities.LibraryActivity;
import com.doozycod.childrenaudiobook.Helper.Model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Activities.ShareYourStoryActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.doozycod.childrenaudiobook.R.drawable.dark_line;
import static com.doozycod.childrenaudiobook.R.drawable.light_line;
import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private Context c;
    private ArrayList<Model> modelArrayList;
    public MediaPlayer mediaPlayer;
    private int length;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;


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
        holder.story_ply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("File for Music", fileModel.getPath());
                ShowMediaPlayerPopoup(fileModel.getPath(), v, i);
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
        ImageView delete_story_btn, share_story_recycler_view_btn, story_ply_btn;

        public RecyclerHolder(@NonNull View itemView) {

            super(itemView);

            book_name_txt = itemView.findViewById(R.id.story_name_txt);
            relativeLayout = itemView.findViewById(R.id.recycler_layout);
            delete_story_btn = itemView.findViewById(R.id.delete_story_btn);
            share_story_recycler_view_btn = itemView.findViewById(R.id.share_story_recycler_view_btn);
            story_ply_btn = itemView.findViewById(R.id.story_play_btn);

        }
    }

    public void ShowPopup(String filePath, View v, int i) {

        Dialog myDialog = new Dialog(c);

        myDialog.setContentView(R.layout.custom_delete_popup);
        ImageView delete_no = myDialog.findViewById(R.id.delete_no);
        ImageView delete_yes = myDialog.findViewById(R.id.delete_yes);

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

    @SuppressLint("ClickableViewAccessibility")
    public void ShowMediaPlayerPopoup(String filePath, View v, int i) {
        SeekBar seekBar;
        Dialog myDialog = new Dialog(c);
        myDialog.setContentView(R.layout.custom_popup_media_player);

        ImageView play_btn = myDialog.findViewById(R.id.play_pause_btn);
        ImageView rewind_btn = myDialog.findViewById(R.id.rewind_btn);
        ImageView ff_btn = myDialog.findViewById(R.id.fast_forward);
        seekBar = myDialog.findViewById(R.id.seekbar);


        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            playLibraryAudio(filePath, seekBar);
        }
        myDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    stopBGMusic();

                return false;
            }
        });

        ff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }

            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    length = mediaPlayer.getCurrentPosition();
                    play_btn.setImageResource(R.drawable.ic_play);

                } else {
                    play_btn.setImageResource(R.drawable.ic_pause);

                    mediaPlayer.seekTo(length);
                    mediaPlayer.start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));
                    }
                }

            }
        });

        rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (currentPosition - seekBackwardTime >= 0) {

                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }


            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(v.getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void playLibraryAudio(String filePath, SeekBar seekBar) {


        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));

            mediaPlayer.setLooping(false);
            Handler mHandler = new Handler();
            ((Activity) c).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    void stopBGMusic() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }


    }

}
