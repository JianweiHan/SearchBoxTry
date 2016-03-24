package com.example.jhan.searchboxtry;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhan on 3/23/16.
 */
public class FavoriteFragment extends ListFragment {
    ArrayList<Business> businessList;
    SimpleAdapter adapter;
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(businessList != null && businessList.size()>0) {
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
            for(int i=0;i<businessList.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name", businessList.get(i).name());
                hm.put("rate", businessList.get(i).rating().toString());
                hm.put("address", businessList.get(i).location().displayAddress().toString().replaceAll("\\[|\\]", "").replaceAll(", USA", ""));
                hm.put("image", businessList.get(i).imageUrl());
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
                            imageLoader.displayImage(businessList.get(position).imageUrl(), im);

                            TextView nameView = (TextView) view.findViewById(R.id.name);
                            nameView.setText(businessList.get(position).name());

                            TextView rateView = (TextView) view.findViewById(R.id.rate);
                            rateView.setText(businessList.get(position).rating().toString());

                            TextView addressView = (TextView) view.findViewById(R.id.address);

                            addressView.setText(businessList.get(position).location().displayAddress().toString().replaceAll("\\[|\\]", "").replaceAll(", USA", ""));
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
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }



}
