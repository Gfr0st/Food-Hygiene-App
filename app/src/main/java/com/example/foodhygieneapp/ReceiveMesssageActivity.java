package com.example.foodhygieneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiveMesssageActivity extends AppCompatActivity {


    private ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
    private RestaurantAdapter restaurantAdpt;

    public static final String EXTRA_MESSAGE = "message";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String sort = "sort";
    public static final String rating = "rating";
    public static final String bussType = "bussType";
    public static final String region = "region";
    public static final String auth = "auth";
    public static final String max_dist = "max_dist";


    public String name;
    public String sort_v;
    public String bussType_v;
    public String rating_v;
    public String region_v;
    public String auth_v;
    public String latitude_v;
    public String longitude_v;
    public String max_dist_v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_messsage);

        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        sort_v = intent.getStringExtra(sort);
        latitude_v = intent.getStringExtra(latitude);
        longitude_v = intent.getStringExtra(longitude);

        restaurantAdpt = new RestaurantAdapter(this,0, restaurantList);
        ListView listView = (ListView) findViewById(R.id.restList);
        listView.setAdapter(restaurantAdpt);

        final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
          public void onItemClick(AdapterView parent, View view, int position, long id)
          {
              Restaurant clickedItem = (Restaurant) restaurantAdpt.getItem(position);
              Intent intent = new Intent(ReceiveMesssageActivity.this, RestaurantDetailsActivity.class);
              intent.putExtra("apiID",clickedItem.getApiID());
              startActivity(intent);
          }
        };
        listView.setOnItemClickListener(itemClickListener);

        callAPI();
    }

    private void callAPI()
    {
        //setting some default values for important search parameters
        if (rating_v == null)
            rating_v = "3";

        if (sort_v == null)
            sort_v = "Relevance";

        if (bussType_v != null)
        {
            if(bussType_v.equals("-1"))
            {
                bussType_v = "";
            }
        }
        else
        {
            bussType_v = "";
        }

        if(auth_v != null)
        {

            if(auth_v.equals("9999"))
            {
                auth_v = "";
            }
        }
        else
        {
            auth_v = "";
        }

        if(max_dist_v == null || max_dist_v.equals(""))
        {
            max_dist_v = "20";
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String allRestaurants = "http://api.ratings.food.gov.uk/Establishments?maxDistanceLimit=" + max_dist_v + "&latitude=" + latitude_v + "&longitude=" + longitude_v + "&businessTypeId="+ bussType_v + "&localAuthorityId="+ auth_v +"&sortOptionKey=" + sort_v + "&ratingOperatorKey=GreaterThanOrEqual&ratingKey="+ rating_v +"&pageNumber=1&pageSize=50&name=" + name;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, allRestaurants, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                            populateList(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.getMessage().toString());
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

    private void populateList(JSONObject obj)
    {
        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        restaurantList.clear();
        try
        {
            JSONArray est = obj.getJSONArray("establishments");
            if(est.length()== 0)
            {
                new AlertDialog.Builder(this)
                        .setMessage(getResources().getText(R.string.no_res))
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
                for(int i =0; i < est.length(); i++)
                {
                    JSONObject restaurant = est.getJSONObject(i);
                    restaurantList.add(new Restaurant(restaurant.getString("BusinessName"), restaurant.getString("FHRSID"), restaurant.getString("RatingValue")));

                }
            }


        } catch (JSONException err){}
        restaurantAdpt.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
    }

    public void onSort(View view)
    {
        Intent intent = new Intent(this, SortActivity.class);
        intent.putExtra(SortActivity.sort, sort_v);
        startActivityForResult(intent, 1);
    }

    public void onFilter(View view)
    {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra(FilterActivity.name, name);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                sort_v = data.getStringExtra(sort);
                callAPI();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else {

            if (requestCode == 2)
            {
              // Write code for filtering
                if (resultCode == Activity.RESULT_OK) {
                    name = data.getStringExtra(EXTRA_MESSAGE);
                    bussType_v = data.getStringExtra(bussType);
                    rating_v = data.getStringExtra(rating);
                    region_v = data.getStringExtra(region);
                    auth_v = data.getStringExtra(auth);
                    max_dist_v = data.getStringExtra(max_dist);
                    callAPI();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(CreateMessageActivity.sort_method, sort_v);
        setResult(Activity.RESULT_OK,returnIntent);
        super.onBackPressed();
    }
}
