package com.example.jhan.searchboxtry;

/**
 * Created by jhan on 3/19/16.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autovalue.shaded.com.google.common.common.collect.Lists;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

//        updateItems();
//
//        Handler responseHandler = new Handler();
//        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
//        mThumbnailDownloader.setThumbnailDownloadListener(
//                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
//                    @Override
//                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
//                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                        photoHolder.bindDrawable(drawable);
//                    }
//                }
//        );
//        mThumbnailDownloader.start();
//        mThumbnailDownloader.getLooper();
//        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
//        mPhotoRecyclerView = (RecyclerView) v
//                .findViewById(R.id.fragment_photo_gallery_recycler_view);
//        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//
//        setupAdapter();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s);
                //updateItems();
                yelpSearch ();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String query = QueryPreferences.getStoredQuery(getActivity());
                //searchView.setQuery(query, false);
            }
        });
    }


    //hjw YelpAPI TRY
    public void yelpSearch ()  {
        YelpAPIFactory apiFactory = new YelpAPIFactory("U3AjEtYCpkP417JC5bnijQ", "Su_kVdBFnF0REg_GhEJgYgYPwoY", "2xOFoqh0dkmi9fSU8GRhZhMGqWrFmMuO", "vmfA12m3OqUg1XeLaoCZvNqfX5k");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

// general params
        params.put("term", "target");
        params.put("limit", "3");

// locale params
        params.put("lang", "fr");

        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);
        try{
            Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                @Override
                public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                    SearchResponse searchResponse = response.body();
                    Log.d(TAG, "也哟: " + "成功内" + searchResponse);

                    int totalNumberOfResult = searchResponse.total();
                    ArrayList<Business> businesses = searchResponse.businesses();
                    String businessName = businesses.get(0).name();  // "JapaCurry Truck"
                    Double rating = businesses.get(0).rating();  // 4.0
                    Log.d(TAG,"totalNumber: " + totalNumberOfResult);
                    Log.d(TAG,"businessName: " + businessName);
                    Log.d(TAG,"rating: " + rating);
                    Log.d(TAG, "ArrayListSize:" + businesses.size());
                    Log.d(TAG, "ArrayList1:" + businesses.get(1).location().displayAddress().toString().replaceAll("\\[|\\]", "").replaceAll(", ","\t"));
                    // Update UI text with the searchResponse.
                }
                @Override
                public void onFailure(Throwable t) {
                    // HTTP error happened, do something to handle it.
                }
            };
            call.enqueue(callback);
            //Response<SearchResponse> response = call.execute();
            //SearchResponse searchResponse = call.execute().body();
            Log.d(TAG, "也哟: " + "成功外");
        }
        catch(Exception e) {
            Log.d(TAG, "也哟: " + "IOException" + e);
        }

    }

}
