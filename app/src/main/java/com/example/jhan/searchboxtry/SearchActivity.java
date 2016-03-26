package com.example.jhan.searchboxtry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SearchActivity extends AppCompatActivity {

    private DrawerLayout mydrawerLayout;
    private ListView mydrawerList;
    private String[] titles = new String[2];
    private ActionBarDrawerToggle mytoggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.twopane);
            mydrawerLayout = (DrawerLayout) findViewById(R.id.mydrawerTwo);
            mydrawerList = (ListView) findViewById(R.id.drawerlistTwo);
        }
        else {
            setContentView(R.layout.activity_search);
            mydrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
            mydrawerList = (ListView) findViewById(R.id.drawerlist);
        }


        titles[0] = "Search";
        titles[1] = "My Favorite";

        mydrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles));
        mydrawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if(position == 0) {
                    mydrawerLayout.closeDrawers();
                    Log.d("******drawer*****88","close");
                }
                if(position == 1) {
                    Log.d("******drawer*****88","favorite");
                    Intent i = new Intent(SearchActivity.this, FavoriteActivity.class);
                    startActivity(i);
                }
            }
        });


        //set actionbar icon
        ActionBar ab =getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //set toggle
        mytoggle = new ActionBarDrawerToggle(this, mydrawerLayout, R.string.opendrawer, R.string.closedrawer) {
            public void onDrawerOpend(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };
        mytoggle.setDrawerIndicatorEnabled(true);
        mydrawerLayout.setDrawerListener(mytoggle);



        //image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);



        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragmentListContainer);

            if (fragment == null) {
                fragment = createFragment();
                fm.beginTransaction()
                        .add(R.id.fragmentListContainer, fragment)
                        .commit();
            }
        }
        else {
            // create fragment manager, and load fragment
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = createFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }



    }


    protected Fragment createFragment() {
        return SearchFragment.newInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mytoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mytoggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mytoggle.onConfigurationChanged(newConfig);
    }
    public boolean isTablet()
    {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;

    }

}
