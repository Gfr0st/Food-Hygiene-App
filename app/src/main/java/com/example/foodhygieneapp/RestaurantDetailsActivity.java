package com.example.foodhygieneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Map;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private String apiID;
    private ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
    private RestaurantAdapter restaurantAdpt;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        sharedPref = this.getSharedPreferences("Preferances-FoodApp", MODE_PRIVATE);

        apiID = getIntent().getStringExtra("apiID");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String alldetails = "http://api.ratings.food.gov.uk/Establishments/" + apiID;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, alldetails, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TextView name = (TextView) findViewById(R.id.name);
                            name.setText(response.getString("BusinessName"));

                            TextView bus = (TextView) findViewById(R.id.businessType);
                            bus.setText(response.getString("BusinessType"));

                            TextView adr1 = (TextView) findViewById(R.id.address1);
                            String test_address = response.getString("AddressLine1");
                            if(test_address == null || test_address.equals(""))
                            {
                                adr1.setVisibility(View.GONE);
                            }
                            else
                            {
                                adr1.setText(test_address);
                            }

                            TextView adr2 = (TextView) findViewById(R.id.address2);
                            String test_address2 = response.getString("AddressLine2");
                            if(test_address2 == null || test_address2.equals(""))
                            {
                                adr2.setVisibility(View.GONE);
                            }
                            else
                            {
                                adr2.setText(test_address2);
                            }


                            TextView adr3 = (TextView) findViewById(R.id.address3);
                            String test_address3 = response.getString("AddressLine3");
                            if(test_address3 == null || test_address3.equals(""))
                            {
                                adr3.setVisibility(View.GONE);
                            }
                            else
                            {
                                adr3.setText(test_address3);
                            }

                            TextView adr4 = (TextView) findViewById(R.id.address4);
                            String test_address4 = response.getString("AddressLine4");
                            if(test_address4 == null || test_address4.equals(""))
                            {
                                adr4.setVisibility(View.GONE);
                            }
                            else
                            {
                                adr4.setText(test_address4);
                            }

                            TextView post = (TextView) findViewById(R.id.post);
                            post.setText(response.getString("PostCode"));

                            TextView date = (TextView) findViewById(R.id.date);
                            String date_s = response.getString("RatingDate");
                            String[] cpy = date_s.split("T");
                            date.setText(cpy[0]);

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


    public void onAddToFav(View view) {

        if (sharedPref.contains(apiID))
        {

            new AlertDialog.Builder(this)
                .setMessage(getResources().getText(R.string.fav_add_nok))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();
        }
        else
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(apiID, apiID);
            editor.commit();

            new AlertDialog.Builder(this)
                    .setMessage(getResources().getText(R.string.fav_add_ok))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();
        }


        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
    }

    public void onRemFromFav(View view) {

        if(sharedPref.contains(apiID))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(apiID);
            editor.commit();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);

            new AlertDialog.Builder(this)
                    .setMessage(getResources().getText(R.string.fav_rem_ok))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();

        }
        else
        {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getText(R.string.fav_rem_nok))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create()
                    .show();

        }


    }
}
