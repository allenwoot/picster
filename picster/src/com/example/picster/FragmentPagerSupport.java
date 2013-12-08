package com.example.picster;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FragmentPagerSupport extends FragmentActivity {
    static final int NUM_ITEMS = 2;

    MyAdapter mAdapter;

    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        // Watch for button clicks.
        //Button button = (Button)findViewById(R.id.goto_first);
        //button.setOnClickListener(new OnClickListener() {
        //    public void onClick(View v) {
        //        mPager.setCurrentItem(0);
        //    }
        //});
        //button = (Button)findViewById(R.id.goto_last);
        //button.setOnClickListener(new OnClickListener() {
        //    public void onClick(View v) {
        //        mPager.setCurrentItem(NUM_ITEMS-1);
        //    }
        //});
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return HorizontalListFragment.newInstance(position);
        }
    }

    public static class HorizontalListFragment extends Fragment {
        int mNum;
        DisplayPictureAdapter displayAdapter;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static HorizontalListFragment newInstance(int num) {
            HorizontalListFragment f = new HorizontalListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_image, container, false);
        
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View tv = getView().findViewById(R.id.user_picture_row);
            ArrayList<PictureGridColumn> columnList = PicsterApplication.currentUser.getColumnList(mNum);
    		this.displayAdapter = new DisplayPictureAdapter(getActivity(), R.layout.picture_list_item, columnList);
        }

        //@Override
        //public void onListItemClick(ListView l, View v, int position, long id) {
        //    Log.i("FragmentList", "Item clicked: " + id);
        //}
    }
}
