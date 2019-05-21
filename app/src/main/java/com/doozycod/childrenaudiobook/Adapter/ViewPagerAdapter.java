package com.doozycod.childrenaudiobook.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.doozycod.childrenaudiobook.Activities.APIService;
import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {


    GridView gridView;
    ChooseYourBookActivity chooseYourBookActivity;
    LayoutInflater mLayoutInflater;
    List<Books_model.book_detail> Book_list_data = new ArrayList<>();
    SharedPreferenceMethod sharedPreferenceMethod;

    public ViewPagerAdapter(ChooseYourBookActivity chooseYourBookActivity, List<Books_model.book_detail> Book_list_data, SharedPreferenceMethod sharedPreferenceMethod) {

        this.sharedPreferenceMethod = sharedPreferenceMethod;
        this.chooseYourBookActivity = chooseYourBookActivity;
        mLayoutInflater = (LayoutInflater) chooseYourBookActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.Book_list_data = Book_list_data;
    }

    @Override
    public int getCount() {
        return Book_list_data.size();
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
        gridView.setAdapter(new GridAdapter(chooseYourBookActivity, Book_list_data,sharedPreferenceMethod));

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}