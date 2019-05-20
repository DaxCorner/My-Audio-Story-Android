package com.doozycod.childrenaudiobook.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.doozycod.childrenaudiobook.Activities.APIService;
import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.API;
import com.doozycod.childrenaudiobook.Utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPagerAdapter extends PagerAdapter {


    GridView gridView;
    ChooseYourBookActivity chooseYourBookActivity;
    LayoutInflater mLayoutInflater;
    int[] grid_icon;
    APIService apiService;
    List<String> book_image = new ArrayList<String>();
    List<String> book_name = new ArrayList<String>();
    List<String> book_audio_file = new ArrayList<String>();

    public ViewPagerAdapter(ChooseYourBookActivity chooseYourBookActivity, int[] grid_icon, APIService apiService, List<String> book_image, List<String> book_name, List<String> book_audio_file) {
        this.chooseYourBookActivity = chooseYourBookActivity;
        mLayoutInflater = (LayoutInflater) chooseYourBookActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.grid_icon = grid_icon;
        this.apiService = apiService;
        this.book_image = book_image;
        this.book_name = book_name;
        this.book_audio_file = book_audio_file;
    }

    @Override
    public int getCount() {
        return grid_icon.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.view_pager_layout, container, false);
        container.addView(itemView);

        gridView = itemView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridAdapter(chooseYourBookActivity, apiService, book_image, book_name, book_audio_file));

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}