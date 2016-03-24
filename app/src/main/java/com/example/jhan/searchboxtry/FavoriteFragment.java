package com.example.jhan.searchboxtry;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;
import com.yelp.clientlib.entities.Deal;
import com.yelp.clientlib.entities.GiftCertificate;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhan on 3/23/16.
 */
public class FavoriteFragment extends ListFragment {

    private static final String TAG = "FavoriteFragment";

    ArrayList<BusinessDataModel> businessList = new ArrayList<>();
    SimpleAdapter adapter;
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        findFavoriteData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(businessList != null && businessList.size()>0) {
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
            for(int i=0;i<businessList.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name", businessList.get(i).bsname);
                hm.put("rate", businessList.get(i).rating);
                hm.put("address", businessList.get(i).address.replaceAll("\\[|\\]", "").replaceAll(", USA", ""));
                hm.put("image", businessList.get(i).imgurl);
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
                        if(businessList.size() > position && !businessList.get(position).bsname.equals("")) {
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            ImageView im = (ImageView) view.findViewById(R.id.image);
                            imageLoader.displayImage(businessList.get(position).imgurl, im);

                            TextView nameView = (TextView) view.findViewById(R.id.name);
                            nameView.setText(businessList.get(position).bsname);

                            TextView rateView = (TextView) view.findViewById(R.id.rate);
                            rateView.setText(businessList.get(position).rating);

                            TextView addressView = (TextView) view.findViewById(R.id.address);

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

                            TextView addressView = (TextView) view.findViewById(R.id.address);
                            addressView.setText("");
                        }
                    }

                    return view;
                }
            };

            setListAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            Intent parentIntentSearch = new Intent(getActivity(), SearchActivity.class);
            startActivity(parentIntentSearch);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void findFavoriteData() {
        DatabaseHelper dbhelper = new DatabaseHelper(this.getContext());
        try{
            Cursor resultset = dbhelper.getReadableDatabase().query("business2", null, null, null, null, null, null);

            resultset.moveToFirst();
            businessList.clear();
            if(resultset.getCount() == 0) {

                for(int i = 0; i < 10; i++) {
                    BusinessDataModel bsModel = new BusinessDataModel();
                    bsModel.bsname = "";
                    bsModel.rating = "";
                    bsModel.imgurl = "";
                    bsModel.address = "";
                    businessList.add(bsModel);
                }

                return;
            }

            while (!resultset.isAfterLast()) {

                BusinessDataModel bsModel = new BusinessDataModel();
                bsModel.bsname = resultset.getString(1);
                bsModel.rating = resultset.getString(2);
                bsModel.imgurl = resultset.getString(3);
                bsModel.ratingimgurl = resultset.getString(4);
                bsModel.reviewcount = resultset.getInt(5);
                bsModel.mapurl = resultset.getString(6);
                bsModel.address = resultset.getString(7);
                bsModel.phone = resultset.getString(8);
                bsModel.snippetimagerul = resultset.getString(9);
                bsModel.snippettext = resultset.getString(10);
                bsModel.latitude = resultset.getString(11);
                bsModel.longtitude = resultset.getString(12);

                businessList.add(bsModel);

                resultset.moveToNext();
            }
        }
        catch(Exception e){
            businessList.clear();
            for(int i = 0; i < 10; i++) {
                BusinessDataModel bsModel = new BusinessDataModel();
                bsModel.bsname = "";
                bsModel.rating = "";
                bsModel.imgurl = "";
                bsModel.address = "";
                businessList.add(bsModel);
            }
        }


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (getActivity().findViewById(R.id.linearLayoutTwoPane) == null) {   /////for phone

            Object m = adapter.getItem(position);
            Log.d(TAG, "object itme is" + m.toString());

            //Intent i = new Intent(getActivity(), DetailActivity.class);
            Intent i = new Intent(getActivity(), DetailPagerActivity.class);
            if (businessList.size() > 0) {
                //i.putExtra("BUSINESS_DATA",businessList.get(position));
                i.putExtra("BUSINESS_DATA", businessList);
            }
            i.putExtra("ITEM_NUMBER", position);
            i.putExtra("PARENT_ACTIVITY", "favorite");
            startActivity(i);
        } else {  //////for tablet
            // create fragment manager, and load fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();

            BusinessDataModel bsModel = businessList.get(position);

            Fragment fragment = DetailFragment.newInstance();

            //set argument for fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("BUSINESS_DATA", bsModel);
            bundle.putString("PARENT_ACTIVITY", "favorite");
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .replace(R.id.detailFragmentContainer, fragment)
                    .commit();


        }
    }
}
