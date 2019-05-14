package com.doozycod.childrenaudiobook.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.R;

public class ViewPagerAdapter extends PagerAdapter {


    GridView gridView;
    ChooseYourBookActivity chooseYourBookActivity;
    LayoutInflater mLayoutInflater;
    int[] grid_icon;
    public static int[] grid_image = {R.drawable.book_01, R.drawable.book_02, R.drawable.book_03, R.drawable.book_04, R.drawable.book_04, R.drawable.book_05};


    public ViewPagerAdapter(ChooseYourBookActivity chooseYourBookActivity, int[] grid_icon) {
        this.chooseYourBookActivity = chooseYourBookActivity;
        mLayoutInflater = (LayoutInflater) chooseYourBookActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.grid_icon = grid_icon;
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
        gridView.setAdapter(new GridAdapter(chooseYourBookActivity, grid_image));

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}