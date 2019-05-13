package com.doozycod.childrenaudiobook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.doozycod.childrenaudiobook.R.drawable.pop_up_bg;

public class MainActivity extends AppCompatActivity {
    public static int[] grid_image = {R.drawable.book_01, R.drawable.book_02, R.drawable.book_03, R.drawable.book_04};

    View view;
    ImageView login_btn, home_btn, library_btn, popup_login, popup_signup, login_dialog;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Dialog myDialog;
    Boolean isPressed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home_btn = findViewById(R.id.home_btn);
        library_btn = findViewById(R.id.lib_btn_main);
        myDialog = new Dialog(this);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        view = new View(this);
        viewPager = findViewById(R.id.photos_viewpager);
        login_btn = findViewById(R.id.login_btn_main);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, RecordOwnAudioActivity.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, grid_image);

        library_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LibraryActivity.class));
            }
        });
        viewPager.setAdapter(viewPagerAdapter);


        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dark_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.light_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        if (isPressed) {
            ShowPopupRating(view);
            isPressed = false;

        } else {
            isPressed = false;
            finish();
        }

    }


    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.custom_popup);
        popup_login = myDialog.findViewById(R.id.select_login);
        popup_signup = myDialog.findViewById(R.id.sign_upact_btn);
        popup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
        popup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                showLoginPopUp(v);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();
    }

    public void showLoginPopUp(View v) {

        myDialog.setContentView(R.layout.custom_login_popup);

        login_dialog = myDialog.findViewById(R.id.login_dialog_btn);

        login_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));
        myDialog.show();
    }

    public void ShowPopupRating(View v) {

        myDialog.setContentView(R.layout.custom_popup_rate_our_app);
        ImageView submit_btn, later_btn;
        submit_btn = myDialog.findViewById(R.id.rating_now_btn);
        later_btn = myDialog.findViewById(R.id.later_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        later_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(pop_up_bg));

        myDialog.show();

    }


}




