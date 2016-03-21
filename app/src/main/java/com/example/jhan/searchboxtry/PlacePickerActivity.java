package com.example.jhan.searchboxtry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Text;

/**
 * Created by huimin on 3/20/16.
 */
public class PlacePickerActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private static final String TAG = PlacePickerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
            Intent intent = intentBuilder.build(PlacePickerActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == PLACE_PICKER_REQUEST
                    && resultCode == Activity.RESULT_OK) {

                final Place place = PlacePicker.getPlace(this, data);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }

//                mName.setText(name);
//                mAddress.setText(address);
//                mAttributions.setText(Html.fromHtml(attributions));
                place.getLatLng();
                //Log.d(TAG, "onActivityResult: " + place.getLatLng().toString());
                Place placepick = PlacePicker.getPlace(data, this);
                LatLng latlng = placepick.getLatLng();
                String toastMsg = String.format("Place: %s", latlng.toString());

//                TextView text = (TextView) findViewById(R.id.textView2);
//                text.setText(toastMsg);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                //add intent to send the LatLng to another activity
//                Intent intent = new Intent(PlacePickerActivity.this, LocationReceiver.class);
////                Bundle b = new Bundle();
////                b.putString("location", toastMsg);
//                intent.putExtra("location", toastMsg);
//                this.startActivity(intent);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
}
