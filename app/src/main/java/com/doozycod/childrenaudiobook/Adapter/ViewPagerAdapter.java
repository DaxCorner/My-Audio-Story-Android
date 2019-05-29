package com.doozycod.childrenaudiobook.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.doozycod.childrenaudiobook.Activities.ChooseYourBookActivity;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {


    GridView gridView;
    private ChooseYourBookActivity chooseYourBookActivity;
    LayoutInflater mLayoutInflater;
    List<Books_model.book_detail> Book_list_data = new ArrayList<>();
    SharedPreferenceMethod sharedPreferenceMethod;
    private ArrayList<View> views = new ArrayList<View>();

    public ViewPagerAdapter(ChooseYourBookActivity chooseYourBookActivity, List<Books_model.book_detail> Book_list_data, SharedPreferenceMethod sharedPreferenceMethod) {

        this.sharedPreferenceMethod = sharedPreferenceMethod;
        this.chooseYourBookActivity = chooseYourBookActivity;
        mLayoutInflater = (LayoutInflater) chooseYourBookActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.Book_list_data = Book_list_data;
    }

    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    public int addView(View v) {
        return addView(v, views.size());
    }

    public int addView(View v, int position) {
        views.add(position, v);
        return position;
    }

    public int removeView(ViewPager pager, View v) {
        return removeView(pager, views.indexOf(v));
    }

    public int removeView(ViewPager pager, int position) {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);

        return position;
    }

    public View getView(int position) {
        return views.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.view_pager_layout, container, false);
//        container.addView(itemView);
        View v = views.get(position);
        container.addView(v);
        gridView = v.findViewById(R.id.gridview);
        gridView.setAdapter(new GridAdapter(chooseYourBookActivity, Book_list_data, sharedPreferenceMethod));

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


}