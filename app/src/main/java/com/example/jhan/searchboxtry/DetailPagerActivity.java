package com.example.jhan.searchboxtry;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class DetailPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    static private ArrayList<Business> businessList = new ArrayList<Business>();
    private int size = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);
        //mViewPager = new ViewPager(this);
        //mViewPager.setId(R.id.pager);
        //setContentView(mViewPager);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        //set actionbar icon
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
        //ab.setTitle("Back");



        //get intent information
        businessList = (ArrayList<Business>)getIntent().getSerializableExtra("BUSINESS_DATA");
        int itemNumber = getIntent().getIntExtra("ITEM_NUMBER", -1);



        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //size = businessList.size();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Fragment detailFragment = DetailFragment.newInstance();
                if(businessList != null && businessList.size() > 0) {
                    Business businessItem = businessList.get(position);
                    Log.d("getItem之内", "position：" + position);
                    //set detailFgrament arguments
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BUSINESS_DATA", businessItem);
                    detailFragment.setArguments(bundle);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BUSINESS_DATA", null);
                    detailFragment.setArguments(bundle);
                }

                return detailFragment;
            }

            @Override
            public int getCount() {
                return size;
            }
        });

        mViewPager.setCurrentItem(itemNumber);
        Log.d("setCurrent之后", "itemNumber：" + itemNumber);


        /*
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int pos, float posOffset, int posOfsetPixels){}
            public void onPageSelected(int pos) {

            }
        }) ;

        */

    }
}
