package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class Add_More_Stock_Items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_more_stock_items);
    }

    public void onClick_Yes (View aView) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, Add_Stock_Item.class));
            finish();
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Add_Stock_Item");
            startActivity(intent);
            finish();
        }
    }

    public void onClick_No (View aView) {
        //starting a new Home_Page activity
        startActivity(new Intent(this, Home_Page.class));
        //ending this activity
        finish();
    }


    //method to check whether connected to internet
    public boolean isInternetConnected( ){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }


    //back button
    public void click_back(View aView){
        finish();
    }

    //home button
    public void click_home(View aView){
        Intent intent = new Intent(getBaseContext(),Home_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
