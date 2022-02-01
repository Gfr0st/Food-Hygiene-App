package com.example.foodhygieneapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

public class FilterActivity extends AppCompatActivity {

    public static final String name = "name";
    public static final String rating = "rating";
    public static final String bussType = "bussType";
    public static final String region = "region";
    public static final String auth_f = "auth";
    public static final String max_dist = "max_dist";

    public static String f_name;
    public static String f_buss;
    public static String f_minr;
    public static String f_auth;
    public static String region_s;
    public static  String f_dist;

    public ArrayList<Authority> auth = new ArrayList<Authority>();
    public ArrayList<Authority> good_auth = new ArrayList<Authority>();
    public ArrayList<BusinessType> buss = new ArrayList<BusinessType>();
    public Spinner sp_auth;
    public Spinner sp_buss;

    private ArrayAdapter<Authority> authAdpt;
    private ArrayAdapter<BusinessType> busAdpt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Intent intent = getIntent();
        f_name = intent.getStringExtra(name);
//        f_minr = intent.getStringExtra(rating);
//        f_buss = intent.getStringExtra(bussType);
//        region_s = intent.getStringExtra(region);
//        f_dist = intent.getStringExtra(max_dist);
//        f_auth = intent.getStringExtra(auth_f);

        Spinner region = findViewById(R.id.f__spinner2);
        region_s = region.getSelectedItem().toString();

        authAdpt = new ArrayAdapter<Authority>(this, android.R.layout.simple_selectable_list_item, good_auth);
        sp_auth = (Spinner) findViewById(R.id.f__spinner3);
        sp_auth.setAdapter(authAdpt);

        busAdpt = new ArrayAdapter<BusinessType>(this, android.R.layout.simple_selectable_list_item, buss);
        sp_buss = (Spinner) findViewById(R.id.f_buss_spinner);
        sp_buss.setAdapter(busAdpt);

        callAuthApi();
        callBusApi();

        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item

                region_s = adapter.getItemAtPosition(position).toString();
                good_auth.clear();
                good_auth.add(new Authority("Any","9999", "Any"));

                if(!region_s.equals("Any"))
                {
                    for(int j =0; j < auth.size(); j++)
                    {
                        if(auth.get(j).getRegion().equals(region_s))
                        {
                            good_auth.add(auth.get(j));
                        }
                    }
                    sp_auth.setSelection(0);
                }
                else {
                    good_auth.addAll(auth);
                }
                authAdpt.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        sp_auth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                String auth_s = adapter.getItemAtPosition(position).toString();

                for(int i =0;i < good_auth.size();i++)
                {
                    if(good_auth.get(i).getName().equals(auth_s))
                    {
                        f_auth = good_auth.get(i).getApiID();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        sp_buss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                String bus_s = adapter.getItemAtPosition(position).toString();

                for(int i =0;i < buss.size();i++)
                {
                    if(buss.get(i).getName().equals(bus_s))
                    {
                        f_buss = buss.get(i).getApiID();
                        Log.e("result",String.valueOf(f_buss));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void callAuthApi()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String allAuth = "http://api.ratings.food.gov.uk/Authorities";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, allAuth, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateAuthorities(response);
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

    private void populateAuthorities(JSONObject obj)
    {
        auth.clear();

        try
        {
            JSONArray est = obj.getJSONArray("authorities");
            for(int i =0; i < est.length(); i++)
            {
                JSONObject authority = est.getJSONObject(i);
                auth.add(new Authority(authority.getString("Name"), authority.getString("LocalAuthorityId"), authority.getString("RegionName")));
            }

        } catch (JSONException err){}


        good_auth.addAll(auth);
        sp_auth.setSelection(0);
        sp_buss.setSelection(0);

        authAdpt.notifyDataSetChanged();

    }

    private void callBusApi()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String allBus = "http://api.ratings.food.gov.uk/BusinessTypes";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, allBus, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateBusinessType(response);
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

    private void populateBusinessType(JSONObject obj)
    {
        buss.clear();

        try
        {
            JSONArray est = obj.getJSONArray("businessTypes");
            for(int i =0; i < est.length(); i++)
            {
                JSONObject business = est.getJSONObject(i);
                buss.add(new BusinessType(business.getString("BusinessTypeName"), business.getString("BusinessTypeId")));
            }

        } catch (JSONException err){}

        busAdpt.notifyDataSetChanged();

    }

    public void onFilterClick(View view) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(ReceiveMesssageActivity.EXTRA_MESSAGE, f_name);

        Spinner rating = findViewById(R.id.f__spinner4);
        f_minr = rating.getSelectedItem().toString();
        returnIntent.putExtra(ReceiveMesssageActivity.rating, f_minr);

        returnIntent.putExtra(ReceiveMesssageActivity.auth, f_auth);

        Spinner region = findViewById(R.id.f__spinner2);
        region_s = region.getSelectedItem().toString();
        returnIntent.putExtra(ReceiveMesssageActivity.region, region_s);

        returnIntent.putExtra(ReceiveMesssageActivity.bussType, f_buss);

        EditText distance= findViewById(R.id.f_editdist);
        f_dist = distance.getText().toString();
        returnIntent.putExtra(ReceiveMesssageActivity.max_dist, f_dist);

        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
