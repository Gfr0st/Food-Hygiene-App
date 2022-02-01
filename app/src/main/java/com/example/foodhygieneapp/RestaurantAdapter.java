package com.example.foodhygieneapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    private Context context;
    private List<Restaurant> restaurantList;

    //constructor, call on creation
    public RestaurantAdapter(Context context, int resource, ArrayList<Restaurant> objects) {

        super(context, resource, objects);
        this.context = context;
        this.restaurantList = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        Restaurant restaurant = restaurantList.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(CreateMessageActivity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.restaurant_layout, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView rating = (TextView) view.findViewById(R.id.rating);


        name.setText(context.getResources().getString(R.string.tv_name) + ": "+ String.valueOf(restaurant.getName()));
        rating.setText(context.getResources().getString(R.string.tv_rating) + ": " + String.valueOf(restaurant.getRating()));


        return view;
    }
}
