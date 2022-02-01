package com.example.foodhygieneapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMessageActivity extends AppCompatActivity {

    public static final String sort_method = "sort_method";
    public static String sort_v;

    private ArrayList<Restaurant> favList = new ArrayList<Restaurant>();
    private ArrayList<String> favIDs = new ArrayList<String>();
    private RestaurantAdapter favAdpt;

    public static String latitude;
    public static String longitude;

    private final int FINE_LOCATION_PERMISSION = 1;
    public LocationListener locListener;
    public LocationManager locManager;

    private SharedPreferences sharedPref;

    public void refresh()
    {
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        sharedPref = this.getSharedPreferences("Preferances-FoodApp", MODE_PRIVATE);
        Map<String,?> keys = sharedPref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            favIDs.add(entry.getValue().toString());
        }

        favAdpt = new RestaurantAdapter(this,0, favList);
        ListView listView = (ListView) findViewById(R.id.favouritelist);
        listView.setAdapter(favAdpt);

        final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                Restaurant clickedItem = (Restaurant) favAdpt.getItem(position);
                Intent intent = new Intent(CreateMessageActivity.this, RestaurantDetailsActivity.class);
                intent.putExtra("apiID",clickedItem.getApiID());
                startActivityForResult(intent,2);
            }
        };

        listView.setOnItemClickListener(itemClickListener);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location)
            {
                //Code to keep track of changing location!
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setMessage(getResources().getText(R.string.prompt_location))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestLocPerms();
                            }
                        })
                        .create()
                        .show();
            } else {
                requestLocPerms();
            }
        } else {
            attachLocManager();
        }

        callFavAPI();
    }

    private void callFavAPI()
    {

        for (int i=0; i < favIDs.size() ;i++)
        {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final String alldetails = "http://api.ratings.food.gov.uk/Establishments/" + favIDs.get(i);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, alldetails, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                favList.add(new Restaurant(response.getString("BusinessName"), response.getString("FHRSID"), response.getString("RatingValue")));
                                favAdpt.notifyDataSetChanged();

                            }catch (JSONException err){}
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("x-api-version", "2");
                    return params;
                }
            };

            requestQueue.add(getRequest);
        }

    }

    public void requestLocPerms() {
        ActivityCompat.requestPermissions(CreateMessageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
    }

    public void attachLocManager()
    {
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);

        } catch (SecurityException err) {}
    }

    public void onSearchRestaurant(View view)
    {
        Intent intent = new Intent(this, ReceiveMesssageActivity.class);
        EditText tv = (EditText) findViewById(R.id.message);
        String name = tv.getText().toString();
        intent.putExtra(ReceiveMesssageActivity.EXTRA_MESSAGE, name);
        intent.putExtra(ReceiveMesssageActivity.sort, sort_v);
        intent.putExtra(ReceiveMesssageActivity.latitude, latitude);
        intent.putExtra(ReceiveMesssageActivity.longitude, longitude);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                sort_v = data.getStringExtra(sort_method);
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                refresh();
            }
        }

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                refresh();
            }
        }
    }
}
