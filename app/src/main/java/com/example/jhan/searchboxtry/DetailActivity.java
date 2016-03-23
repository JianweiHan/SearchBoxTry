package com.example.jhan.searchboxtry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        //set actionbar icon
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
        //ab.setTitle("Back");

        //image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);


        // create fragment manager, and load fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detailfragment_container);

        business = (Business)getIntent().getSerializableExtra("BUSINESS_DATA");


        if (fragment == null) {
            fragment = createFragment();

            //set argument for fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("BUSINESS_DATA", business);
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.detailfragment_container, fragment)
                    .commit();
        }


    }

    protected Fragment createFragment() {
        return DetailFragment.newInstance();
    }
}
