package com.example.jhan.searchboxtry;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class DetailPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    static private ArrayList<Business> businessList = new ArrayList<>();
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);
        //mViewPager = new ViewPager(this);
        //mViewPager.setId(R.id.pager);
        //setContentView(mViewPager);
        mViewPager = (ViewPager) findViewById(R.id.pager);


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        size = businessList.size();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Business businessItem = businessList.get(position);
                Log.d("getItem之内", "position："+ position);
                return DetailFragment.newInstance();
            }

            @Override
            public int getCount() {
                return size;
            }
        });


        businessList = (ArrayList<Business>)getIntent().getSerializableExtra("BUSINESS_DATA");
        int itemNumber = getIntent().getIntExtra("ITEM_NUMBER", -1);

        mViewPager.setCurrentItem(itemNumber + 1);
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
