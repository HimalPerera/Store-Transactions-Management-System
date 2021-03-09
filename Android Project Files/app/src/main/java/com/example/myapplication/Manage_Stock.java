package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class Manage_Stock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_stock);
    }

    //open Add Stock Item activity
    public void open_Add_Stock_Item(View aView) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, Add_Stock_Item.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Add_Stock_Item");
            startActivity(intent);
        }
    }

    //open Remove Stock Item activity
    public void open_Remove_Stock_Item(View aView) {
        if (isInternetConnected()) {
            startActivity(new Intent(this, Remove_Stock_Item.class));
        } else {
            Intent intent = new Intent(getBaseContext(), No_Internet.class);
            intent.putExtra("Destination", "Remove_Stock_Item");
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
