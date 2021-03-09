package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class No_Internet extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);
    }


    public void clickRetry(View aView) {
        if (isInternetConnected()) {
            String destination = getIntent().getStringExtra("Destination");

            switch (destination) {
                case "Remove_Stock_Item":
                    startActivity(new Intent(getBaseContext(), Remove_Stock_Item.class));
                    finish();
                    break;
                case "Add_Stock_Item":
                    startActivity(new Intent(getBaseContext(), Add_Stock_Item.class));
                    finish();
                    break;
                case "Purchase":
                    startActivity(new Intent(getBaseContext(), Purchase.class));
                    finish();
                    break;
                case "Sell":
                    startActivity(new Intent(getBaseContext(), Sell.class));
                    finish();
                    break;
                case "PurchasingDetails":
                    startActivity(new Intent(getBaseContext(), View_Purchases.class));
                    finish();
                    break;
                case "SalesDetails":
                    startActivity(new Intent(getBaseContext(), View_Sales.class));
                    finish();
                    break;
                case "StockDetails":
                    startActivity(new Intent(getBaseContext(), View_Stock.class));
                    finish();
                    break;
            }

        } else {
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
