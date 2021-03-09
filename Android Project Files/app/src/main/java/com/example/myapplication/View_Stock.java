package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View_Stock extends AppCompatActivity {

    //declaring variables
    ListView LV_displayStock;
    TextView TV_noOfStockItems;
    JSONArray allStock_JSONArray = null;
    ArrayList<Stock_Item> allStock_ArrayList = new ArrayList<Stock_Item>();
    String[] StockList_Array = null;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stock);


        //instantiating variables
        LV_displayStock = (ListView) findViewById(R.id.listView_displayStock);
        TV_noOfStockItems = (TextView) findViewById(R.id.textView_noOfStockItems);


        //displaying progress dialog while data is retrieved from database
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Retrieving Data");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Runnable progress = new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };
        Handler canceller = new Handler();
        canceller.postDelayed(progress, 3000);


        //list view listeners
        LV_displayStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = allStock_ArrayList.get(position).toString();
                Toast.makeText(getApplicationContext(), details, Toast.LENGTH_LONG).show();
            }
        });


        //getting all stock items from stock database, load them into arrays and display them
        loadInToArrays();
        displayDetails();
    }


    //method to load stock details in to arrays
    public void loadInToArrays() {

        Get_All_Stock_Items ob = new Get_All_Stock_Items(this);
        allStock_JSONArray = ob.get_All_Items();

        //checking whether the returned JSONArray is empty due to connection issues
        try {
            int length = allStock_JSONArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(this, "Failed to connect to the database", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }


        if (allStock_JSONArray == null) {
            //display an alert dialog box to inform database is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Failed");
            builder.setMessage("Stock Database is empty");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), Home_Page.class));
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        //defining the length for String array which is the list of ListView
        StockList_Array = new String[allStock_JSONArray.length()];


        //inserting data into the arrayList from the JSONArray
        for (int i = 0; i < allStock_JSONArray.length(); i++) {
            JSONObject aStockItem = null;
            Stock_Item stock_item = null;

            try {
                aStockItem = allStock_JSONArray.getJSONObject(i);

                String itemCode = aStockItem.getString("itemCode");
                String itemName = aStockItem.getString("itemName");
                double unitPrice = aStockItem.getDouble("unitPrice");
                double discountRate = aStockItem.getDouble("discountRate");
                int discountLevel = aStockItem.getInt("discountLevel");
                int currentStock = aStockItem.getInt("currentStock");

                //inserting itemCodes and current stock into the String array for the labels of ListView
                StockList_Array[i] = "Item Code : " + itemCode + "\n" + "Current Stock : " + currentStock;

                //creating a new Stock_Item and adding it to the arrayList
                stock_item = new Stock_Item(itemCode, itemName, unitPrice, discountRate, discountLevel, currentStock);
                allStock_ArrayList.add(stock_item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    //method to dispalay the details in the list view
    public void displayDetails() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StockList_Array);
        LV_displayStock.setAdapter(arrayAdapter);

        TV_noOfStockItems.setText("No. of Stock Items : " + String.valueOf(allStock_ArrayList.size()));
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
