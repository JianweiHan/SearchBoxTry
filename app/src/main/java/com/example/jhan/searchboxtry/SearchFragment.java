package com.example.jhan.searchboxtry;

/**
 * Created by jhan on 3/19/16.
 */

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

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

public class SearchFragment extends ListFragment {

    static ArrayList<BusinessDataModel> businessList = new ArrayList<BusinessDataModel>();
    static Double currentLat = Double.MAX_VALUE;
    static Double currentLong = Double.MAX_VALUE;

    static int[] initImage = new int[20];
    static String[] initName = new String[20];
    static String[] initRate = new String[20];
    static String[] initAddress = new String[20];
/*
    // Array of strings storing country names
    String[] countries = new String[] {
            "India",
            "Pakistan",
            "Sri Lanka",
            "China",
            "Bangladesh",
            "Nepal",
            "Afghanistan",
            "North Korea",
            "South Korea",
            "Japan"
    };

    // Array of integers points to images stored in /res/drawable/
    int[] flags = new int[]{
            R.drawable.india,
            R.drawable.pakistan,
            R.drawable.srilanka,
            R.drawable.china,
            R.drawable.bangladesh,
            R.drawable.nepal,
            R.drawable.afghanistan,
            R.drawable.nkorea,
            R.drawable.skorea,
            R.drawable.japan
    };

    // Array of strings to store currencies
    String[] currency = new String[]{
            "Indian Rupee",
            "Pakistani Rupee",
            "Sri Lankan Rupee",
            "Renminbi",
            "Bangladeshi Taka",
            "Nepalese Rupee",
            "Afghani",
            "North Korean Won",
            "South Korean Won",
            "Japanese Yen"
    };


*/
   // ArrayList<String> imageUrlList = new ArrayList<String>();
   // ArrayList<String> nameList = new ArrayList<String>();
   // ArrayList<Double> ratingList = new ArrayList<Double>();
   // ArrayList<ArrayList<String>> addressList = new ArrayList<>();
    String strTerm; //search term
    SimpleAdapter adapter;


    private static final String TAG = "SearchFragment";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);


        for(int i = 0; i < 20; i++) {
            initImage[i] = R.drawable.blank;
            initName[i] = "";
            initRate[i] = "";
            initAddress[i] = "";
        }

        currentLat= getActivity().getIntent().getDoubleExtra("LATITUDE", Double.MAX_VALUE);
        currentLong= getActivity().getIntent().getDoubleExtra("LONGITUDE", Double.MAX_VALUE);
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
       // View v = inflater.inflate(R.layout.fragment_search, container, false);
//        mPhotoRecyclerView = (RecyclerView) v
//                .findViewById(R.id.fragment_photo_gallery_recycler_view);
//        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//
//        setupAdapter();

       // return v;



        // Each row in the list stores country name, currency and flag

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<20;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name", initName[i]);
            hm.put("rate", initRate[i]);
            hm.put("address", initAddress[i]);
            hm.put("image", Integer.toString(initImage[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "image","name","rate", "address" };

        // Ids of views in listview_layout
        int[] to = { R.id.image, R.id.name, R.id.rate, R.id.address};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        //SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to) {
        adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to) {
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {


                View view = super.getView(position, convertView, parent);
                if(businessList.size() > 0) {
                    if(businessList.size() > position) {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        ImageView im = (ImageView) view.findViewById(R.id.image);
                        imageLoader.displayImage(businessList.get(position).imgurl, im);

                        TextView nameView = (TextView) view.findViewById(R.id.name);
                        nameView.setText(businessList.get(position).bsname);

                        TextView rateView = (TextView) view.findViewById(R.id.rate);
                        rateView.setText(businessList.get(position).rating);

                        TextView addressView = (TextView) view.findViewById(R.id.address);
                        /*
                        String addressShow = "";
                        Log.d(TAG, addressList.get(position).toString());
                        Log.d(TAG, addressList.get(position).get(0));
                        Log.d(TAG, addressList.get(position).get(1));
                        for(int i = 0; i < 2; i++) {
                            addressShow += addressList.get(position).get(i) + " ";
                        }
                        addressView.setText(addressShow);
                        */
                        addressView.setText(businessList.get(position).address.replaceAll("\\[|\\]", "").replaceAll(", USA", ""));
                    }
                    else {  // if there are less than 20 items return from yelp api, display the default image
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        ImageView im = (ImageView) view.findViewById(R.id.image);
                        //imageLoader.displayImage("drawable://" + R.drawable.blank, im);
                        im.setImageResource(R.drawable.blank);

                        TextView nameView = (TextView) view.findViewById(R.id.name);
                        nameView.setText("");

                        TextView rateView = (TextView) view.findViewById(R.id.rate);
                        rateView.setText("");
                    }
                }

                return view;
            }
        };

        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        return super.onCreateView(inflater, container, savedInstanceState);




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_search_menu, menu);

        //1. search menu
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s);
                //updateItems();
                strTerm = s;
                yelpSearch (strTerm, 0);
                searchView.onActionViewCollapsed();
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
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //2. sortBydistance menu
        if (id == R.id.menu_item_sortDistace) {
            if(strTerm != null) {
                yelpSearch(strTerm, 1);
            }
            return true;
        }

        //3. sortByrelevance menu
        if (id == R.id.menu_item_sortRelevance) {
            if(strTerm != null) {
                yelpSearch(strTerm, 0);
            }
            return true;
        }

        if(id == R.id.menu_item_placePicker) {

            Intent i = new Intent(getActivity(), PlacePickerActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.menu_item_favoriteActivity) {
            Intent i = new Intent(getActivity(), FavoriteActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    //hjw YelpAPI TRY
    public void yelpSearch (String term, int sort)  {
        YelpAPIFactory apiFactory = new YelpAPIFactory("U3AjEtYCpkP417JC5bnijQ", "Su_kVdBFnF0REg_GhEJgYgYPwoY", "2xOFoqh0dkmi9fSU8GRhZhMGqWrFmMuO", "vmfA12m3OqUg1XeLaoCZvNqfX5k");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

// general params
        params.put("term", term);
        params.put("limit", "20");
        params.put("sort", String.valueOf(sort)); //Sort mode: 0=Best matched (default), 1=Distance, 2=Highest Rated. The rating sort is not strictly sorted by the rating value, but by an adjusted rating value that takes into account the number of ratings, similar to a bayesian average. This is so a business with 1 rating of 5 stars doesn’t immediately jump to the top.
        params.put("radius_filter", "16093.4"); // in meters, this is 10 miles
// locale params
        params.put("lang", "fr");

        Call<SearchResponse> call;
        if(currentLat == Double.MAX_VALUE|| currentLong == Double.MAX_VALUE) {
            call = yelpAPI.search("San Jose", params);
        }
        else {
            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(currentLat)
                    .longitude(currentLong).build();
            call = yelpAPI.search(coordinate, params);
        }
        try{
            Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                @Override
                public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                    SearchResponse searchResponse = response.body();
                    Log.d(TAG, "也哟: " + "成功内" + searchResponse);

                    int totalNumberOfResult = searchResponse.total();
                    businessList.clear();

                    ArrayList<Business> businesses = searchResponse.businesses();
                    for(Business businessItem: businesses) {
                        BusinessDataModel bsModel = new BusinessDataModel();
                        bsModel.bsname = businessItem.name();
                        bsModel.rating = businessItem.rating().toString();
                        bsModel.imgurl = businessItem.imageUrl();
                        bsModel.ratingimgurl = businessItem.ratingImgUrlLarge();
                        bsModel.reviewcount = businessItem.reviewCount();
                        bsModel.mapurl = "https://maps.googleapis.com/maps/api/staticmap?center=" + businessItem.location().coordinate().latitude() + "," + businessItem.location().coordinate().longitude() + "&zoom=20&size=2600x300&maptype=roadmap&markers=color:red%7Clabel:name%7C" + businessItem.location().coordinate().latitude() + "," + businessItem.location().coordinate().longitude();
                        bsModel.address = businessItem.location().displayAddress().toString();
                        bsModel.phone = businessItem.phone();
                        bsModel.snippetimagerul = businessItem.snippetImageUrl();
                        bsModel.snippettext = businessItem.snippetText();
                        bsModel.latitude = businessItem.location().coordinate().latitude().toString();
                        bsModel.longtitude = businessItem.location().coordinate().longitude().toString();
                        businessList.add(bsModel);
                    }


                    String businessName = businesses.get(0).name();  // "JapaCurry Truck"
                    Double rating = businesses.get(0).rating();  // 4.0
                    Log.d(TAG,"totalNumber: " + totalNumberOfResult);
                    Log.d(TAG,"businessName: " + businessName);
                    Log.d(TAG,"rating: " + rating);
                    Log.d(TAG, "ArrayListSize:" + businesses.size());
                    Log.d(TAG, "ArrayList1:" + businesses.get(1).location().displayAddress().toString().replaceAll("\\[|\\]", "").replaceAll(", ", "\t"));
                    // Update UI text with the searchResponse.

                    /*
                    imageUrlList.clear();
                    nameList.clear();
                    ratingList.clear();
                    addressList.clear();
                    for(Business item: businesses) {
                        imageUrlList.add(item.imageUrl());
                        nameList.add(item.name());
                        ratingList.add(item.rating());
                        addressList.add(new ArrayList<String>(item.location().displayAddress()));
                    }
                    */
                    adapter.notifyDataSetChanged();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("is attached", ":" + (getActivity().findViewById(R.id.linearLayoutTwoPane) == null));
        //Log.d("当前view", ":" + getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getId()+ "resouce:" + R.layout.twopane);


        if(businessList.size() == 0) {
            return;
        }
        if(getActivity().findViewById(R.id.linearLayoutTwoPane) == null) {   /////for phone

            Object m = adapter.getItem(position);
            Log.d(TAG, "object itme is" + m.toString());

            //Intent i = new Intent(getActivity(), DetailActivity.class);
            Intent i = new Intent(getActivity(), DetailPagerActivity.class);
            if(businessList.size() > 0) {
                //i.putExtra("BUSINESS_DATA",businessList.get(position));
                i.putExtra("BUSINESS_DATA", businessList);
            }
            i.putExtra("ITEM_NUMBER", position);
            i.putExtra("PARENT_ACTIVITY", "search");
            startActivity(i);
        }
        else {  //////for tablet
            // create fragment manager, and load fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();

            BusinessDataModel bsModel = businessList.get(position);

                Fragment fragment = DetailFragment.newInstance();

                //set argument for fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("BUSINESS_DATA", bsModel);
                bundle.putString("PARENT_ACTIVITY", "search");
                fragment.setArguments(bundle);

                fm.beginTransaction()
                        .replace(R.id.detailFragmentContainer, fragment)
                        .commit();


        }



    }

}
