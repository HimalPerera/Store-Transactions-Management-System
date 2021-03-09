package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class Remove_More_Stock_Items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_more_stock_items);
    }

    public void onClick_Yes (View aView) {
        if (isInternetConnected()) {
            //starting a new Remove_Stock_Item activity
            startActivity(new Intent(getApplicationContext(), Remove_Stock_Item.class));
            //ending this activity
            finish();
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Remove_Stock_Item");
            startActivity(intent);
            finish();
        }

    }

    public void onClick_No (View aView) {
        startActivity(new Intent(getApplicationContext(), Home_Page.class));
        finish();
    }


    //method to check whether connected to internet
    public boolean isInternetConnected( ) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
