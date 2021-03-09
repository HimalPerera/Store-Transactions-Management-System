package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class View_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);
    }

    //open View_Purchases activity
    public void open_PurchasingDetails(View view) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, View_Purchases.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "PurchasingDetails");
            startActivity(intent);
        }
    }

    //open View_Sales activity
    public void open_SalesDetails(View view) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, View_Sales.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "SalesDetails");
            startActivity(intent);
        }
    }

    //open View_Stock activity
    public void open_StockDetails(View view) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, View_Stock.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "StockDetails");
            startActivity(intent);
        }
    }


    //method to check whether connected to internet
    public boolean isInternetConnected() {
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
