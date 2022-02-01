package com.example.foodhygieneapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class SortActivity extends AppCompatActivity {

    public static final String sort = "sort";

    public static String sort_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        Intent intent = getIntent();
        sort_v = intent.getStringExtra(sort);
        switch(sort_v) {
            case "alpha":
                RadioButton rb = findViewById(R.id.s_rb);
                rb.setChecked(true);
                break;
            case "desc_alpha":
                RadioButton rb2 = findViewById(R.id.s_rb2);
                rb2.setChecked(true);
                break;
            case "rating":
                RadioButton rb3 = findViewById(R.id.s_rb3);
                rb3.setChecked(true);
                break;
            case "distance":
                RadioButton rb4 = findViewById(R.id.s_rb4);
                rb4.setChecked(true);
                break;
            case "Relevance":
                RadioButton rb5 = findViewById(R.id.s_rb5);
                rb5.setChecked(true);
                break;
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.s_rb:
                if (checked)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ReceiveMesssageActivity.sort, "alpha");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                break;
            case R.id.s_rb2:
                if (checked)
                {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ReceiveMesssageActivity.sort,"desc_alpha");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
            case R.id.s_rb3:
                if (checked)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ReceiveMesssageActivity.sort,"rating");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
            case R.id.s_rb4:
                if (checked)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ReceiveMesssageActivity.sort,"distance");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
            case R.id.s_rb5:
                if (checked)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ReceiveMesssageActivity.sort,"Relevance");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

                break;
        }
    }
}
