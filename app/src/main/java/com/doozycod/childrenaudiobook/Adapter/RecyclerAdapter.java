package com.doozycod.childrenaudiobook.Adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doozycod.childrenaudiobook.Activities.APIService;
import com.doozycod.childrenaudiobook.Models.LibraryModel;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Activities.ShareYourStoryActivity;
import com.doozycod.childrenaudiobook.Utils.CustomProgressBar;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doozycod.childrenaudiobook.R.drawable.dark_line;
import static com.doozycod.childrenaudiobook.R.drawable.light_line;
import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private Context c;
    private ArrayList<LibraryModel.LibraryDetails> modelArrayList;
    public MediaPlayer mediaPlayer;
    private int length;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;
    APIService apiService;
    List<LibraryModel.LibraryDetails> libraryModels;
    SharedPreferenceMethod sharedPreferenceMethod;
    Bundle extras = new Bundle();

    public RecyclerAdapter(Context c, List<LibraryModel.LibraryDetails> libraryModels, APIService apiService, SharedPreferenceMethod sharedPreferenceMethod) {
        this.c = c;
        this.libraryModels = libraryModels;
        this.apiService = apiService;
        this.sharedPreferenceMethod = sharedPreferenceMethod;
    }


    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_view, viewGroup, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int i) {

        final LibraryModel.LibraryDetails fileModel = this.libraryModels.get(i);
        Typeface custom_font = Typeface.createFromAsset(c.getAssets(), "fonts/helvetica.ttf");


        holder.book_name_txt.setTypeface(custom_font);
        holder.book_name_txt.setText(fileModel.getName());

        if (i % 2 == 1) {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(light_line));
        } else {
            holder.relativeLayout.setBackground(holder.relativeLayout.getResources().getDrawable(dark_line));

        }
        holder.share_story_recycler_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(c, ShareYourStoryActivity.class));
                extras.putString("book_id", fileModel.book_details.getId());
                extras.putString("audioFile", fileModel.getAudio_story());
                extras.putString("greetingFile", fileModel.getAudio_message());
                extras.putString("audio_filename", fileModel.getName());
                intent.putExtras(extras);
                c.startActivity(intent);
            }
        });
        holder.delete_story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(fileModel.getAudio_story(), v, i, fileModel.getLibrary_id());

            }
        });
        holder.story_ply_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.e("File for Music", fileModel.getAudio_story());

                if (fileModel.getAudio_message().equals("")) {
                    ShowMediaPlayerPopoup("", fileModel.getAudio_story(), v, i, fileModel.getName(), fileModel.book_details.getBook_image());
                } else {
                    ShowMediaPlayerPopoup(fileModel.getAudio_message(), fileModel.getAudio_story(), v, i, fileModel.getName(), fileModel.book_details.getBook_image());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return libraryModels.size();
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

    public void DeleteLibrary(String library_id) {


        apiService.delete_LibraryBook(sharedPreferenceMethod.getUserId(), library_id).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                Log.e("Response", response.body().getSuccess() + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

            }
        });

    }

    public void ShowPopup(String filePath, View v, int i, String Library_id) {

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
                DeleteLibrary(Library_id);
                libraryModels.remove(i);
                myDialog.dismiss();
                notifyItemRemoved(i);

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(v.getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    public void ShowMediaPlayerPopoup(String greeting, String filePath, View v, int i, String audioFileName, String book_image) {
        SeekBar seekBar;
        Dialog myDialog = new Dialog(c, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        myDialog.setContentView(R.layout.custom_popup_media_player_with_skipbtn);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        ImageView play_btn = myDialog.findViewById(R.id.play_pause_btn);
        ImageView rewind_btn = myDialog.findViewById(R.id.rewind_btn);
        ImageView ff_btn = myDialog.findViewById(R.id.fast_forward);
        ImageView back_btn = myDialog.findViewById(R.id.back_press_player);
        ImageView book_img = myDialog.findViewById(R.id.book_img_player);
        Button skip_btn = myDialog.findViewById(R.id.skip_btn_skip);
        TextView audio_fileName = myDialog.findViewById(R.id.audio_file_name);
        seekBar = myDialog.findViewById(R.id.seekbar);
        if (greeting.equals("")) {
            skip_btn.setVisibility(View.GONE);
        } else {
            skip_btn.setVisibility(View.VISIBLE);
        }
        Typeface custom_font = Typeface.createFromAsset(c.getAssets(), "fonts/helvetica.ttf");

        audio_fileName.setTypeface(custom_font);
        if (greeting.equals("")) {
            audio_fileName.setText(audioFileName);
        } else {
            audio_fileName.setText("Greeting");
        }

        mediaPlayer = new MediaPlayer();

        if (greeting.equals("")) {
            if (mediaPlayer.isPlaying()) {
                stopBGMusic();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                playLibraryAudioStory(filePath, seekBar, play_btn);
            }
        } else {
            playLibraryAudioMessage(greeting, seekBar, play_btn);
            skip_btn.setVisibility(View.VISIBLE);

            int duration = mediaPlayer.getDuration();


            Log.e("Duration", duration / 1000 + "");
            if (mediaPlayer.getCurrentPosition() == duration) {
                skip_btn.setVisibility(View.GONE);
                if (mediaPlayer.isPlaying()) {
                    stopBGMusic();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    playLibraryAudioStory(filePath, seekBar, play_btn);
                }

            }
            skip_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audio_fileName.setText(audioFileName);
                    if (mediaPlayer.isPlaying()) {
                        stopBGMusic();
                    }
                    skip_btn.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        playLibraryAudioStory(filePath, seekBar, play_btn);

                    }


                }
            });

        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGMusic();
                myDialog.dismiss();

            }
        });
        Glide.with(c).load("http://" + book_image).into(book_img);
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
                    play_btn.setImageResource(R.drawable.play_button);

                } else {
                    play_btn.setImageResource(R.drawable.pause_button);

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


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void playLibraryAudioMessage(String filePath, SeekBar seekBar, ImageView play_btn) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource("http://" + filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));

            mediaPlayer.setLooping(false);
            Handler mHandler = new Handler();
//            MediaPlayer finalMediaPlayer = mediaPlayer;
            ((Activity) c).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        seekBar.setProgress(mCurrentPosition);
                        if (mCurrentPosition == mediaPlayer.getDuration() / 1000) {
                            play_btn.setImageResource(R.drawable.play_button);

                        }

                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void playLibraryAudioStory(String filePath, SeekBar seekBar, ImageView play_btn) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource("http://" + filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));

            mediaPlayer.setLooping(false);
            Handler mHandler = new Handler();
//            MediaPlayer finalMediaPlayer = mediaPlayer;
            ((Activity) c).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        seekBar.setProgress(mCurrentPosition);
                        if (mCurrentPosition == mediaPlayer.getDuration() / 1000) {
                            play_btn.setImageResource(R.drawable.play_button);

                        }

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
            mediaPlayer.reset();
            mediaPlayer = null;
        }


    }


}
