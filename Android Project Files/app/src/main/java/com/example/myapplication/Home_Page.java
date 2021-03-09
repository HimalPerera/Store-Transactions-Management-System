package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class Home_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }

    //open Manage Stock activity
    public  void  open_Manage_Stock (View view){
        startActivity(new Intent(this,Manage_Stock.class));
    }

    //open Purchase activity
    public  void  open_Purchase (View view){
        if (isInternetConnected()) {
            startActivity(new Intent(this,Purchase.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Purchase");
            startActivity(intent);
        }
    }

    //open Sell activity
    public  void  open_Sell (View view){
        if (isInternetConnected()) {
            startActivity(new Intent(this,Sell.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Sell");
            startActivity(intent);
        }
    }

    //open View_Details activity
    public  void  open_View_Details (View view){
        startActivity(new Intent(this,View_Details.class));}


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


}
