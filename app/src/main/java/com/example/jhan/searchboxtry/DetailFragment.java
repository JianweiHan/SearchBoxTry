package com.example.jhan.searchboxtry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yelp.clientlib.entities.Business;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhan on 3/21/16.
 */
public class DetailFragment extends Fragment {

    private Business business;


    public static DetailFragment newInstance() {
        return new DetailFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        this.business =(Business)getArguments().getSerializable("BUSINESS_DATA");
        /*
        businessList = (ArrayList<Business>)getActivity().getIntent().getSerializableExtra("BUSINESS_DATA");

        itemNumber = getActivity().getIntent().getIntExtra("ITEM_NUMBER", -1);

        Log.d("细节部分", "号码：" + itemNumber);
        if(businessList.size() > 0)
            Log.d("细节部分", "数据组：" + businessList.get(itemNumber).toString());
         */
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        if(business != null) {

            TextView nameView = (TextView)v.findViewById(R.id.name_detail);
            nameView.setText(business.name());

            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView ratingImage = (ImageView)v.findViewById(R.id.ratingImage_detail);
            imageLoader.displayImage(business.ratingImgUrlLarge(), ratingImage);

            TextView reviewView = (TextView)v.findViewById(R.id.reviews_detail);
            reviewView.setText(business.reviewCount() + " Reviews");

            imageLoader = ImageLoader.getInstance();
            ImageView mapImage = (ImageView)v.findViewById(R.id.mapImage_detail);
            String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + business.location().coordinate().latitude() + "," + business.location().coordinate().longitude() + "&zoom=20&size=2600x300&maptype=roadmap&markers=color:red%7Clabel:name%7C" + business.location().coordinate().latitude() + "," + business.location().coordinate().longitude();
            imageLoader.displayImage(mapUrl, mapImage);

            TextView addressView = (TextView)v.findViewById(R.id.address_detail);
            addressView.setText(business.location().displayAddress().toString().replaceAll("\\[|\\]", "").replaceAll(", USA", ""));

            TextView phoneView = (TextView)v.findViewById(R.id.phone_detail);
            phoneView.setText("Phone: " + business.phone());

            imageLoader = ImageLoader.getInstance();
            ImageView snippetImage = (ImageView)v.findViewById(R.id.snippetImage_detail);
            imageLoader.displayImage(business.snippetImageUrl(), snippetImage);

            TextView snippetTextView = (TextView)v.findViewById(R.id.snippetText_detail);
            snippetTextView.setText(business.snippetText());
        }
        return v;

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

}
