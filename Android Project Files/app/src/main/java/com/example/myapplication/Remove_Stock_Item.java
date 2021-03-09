package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Remove_Stock_Item extends AppCompatActivity {

    //declaring variables
    TextView TV_details;
    Spinner SP_itemCode;
    JSONArray stockList_JSONArray;
    ArrayList<Stock_Item> stockList_ArrayList = new ArrayList();
    String [] itemCodeList = null;

    ProgressDialog progressDialog;
    Button Btn_Remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_stock_item);


        //instantiating variables
        TV_details = (TextView) findViewById(R.id.textView_details);
        SP_itemCode = (Spinner) findViewById(R.id.spinner_itemCode);
        Btn_Remove = (Button) findViewById(R.id.button_remove);


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


        //spinner listeners
        SP_itemCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Select an Item Code", Toast.LENGTH_SHORT).show();
            }
        });





        //first of all call loadData method to retrieve data from Database through PHP file
        loadData();


    }


    //remove stock item from database
    public void RemoveItem(View aView) {

        if (!isInternetConnected()) {
            //display an alert dialog box to inform no internet connection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet");
            builder.setMessage("Internet connection is required");
            builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Btn_Remove.performClick();
                }
            });
            builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), Home_Page.class));
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {

            //defining type of method for Background_Worker class
            String type = "REMOVE_STOCK_ITEM";

            //getting the itemCode of selected stock item
            String itemCode = SP_itemCode.getSelectedItem().toString();


            //passing the type and itemCode to a Background_Worker
            Background_Worker background_worker = new Background_Worker();
            background_worker.execute(type, itemCode);

            //display the result coming from PHP file
            try {
                Toast.makeText(getApplicationContext(), background_worker.get(), Toast.LENGTH_LONG).show();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }


            //start a new activity of Remove_Stock_Items
            startActivity(new Intent(getApplicationContext(), Remove_More_Stock_Items.class));
            //ending this activity
            finish();

        }
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


    //method to get all stock items from database and load them into arrays and show in the spinner
    public void loadData () {

        Get_All_Stock_Items ob = new Get_All_Stock_Items(getApplicationContext());
        stockList_JSONArray = ob.get_All_Items();


        //checking whether the returned JSONArray is empty due to connection issues
        try {
            int length = stockList_JSONArray.length();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Failed to connect to the database", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Manage_Stock.class));
            finish();
            return;
        }


        if (stockList_JSONArray == null) {
            //display an alert dialog box to inform database is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Failed");
            builder.setMessage("Stock Database is empty");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Manage_Stock.class));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        //defining the length for String array
        itemCodeList = new String[stockList_JSONArray.length()];


        //inserting data into the arrayList from the JSONArray
        for (int i = 0; i < stockList_JSONArray.length(); i++) {
            JSONObject item = null;
            Stock_Item stock_item = null;

            try {
                item = stockList_JSONArray.getJSONObject(i);

                String itemCode = item.getString("itemCode");
                String itemName = item.getString("itemName");
                double unitPrice = item.getDouble("unitPrice");
                double discountRate = item.getDouble("discountRate");
                int discountLevel = item.getInt("discountLevel");
                int currentStock = item.getInt("currentStock");

                //inserting itemCodes into the String array for the labels of Spinner
                itemCodeList[i] = itemCode;

                //creating a new Stock_Item and adding it to the arrayList
                stock_item = new Stock_Item(itemCode, itemName, unitPrice, discountRate, discountLevel, currentStock);
                stockList_ArrayList.add(stock_item);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //setting the array adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, itemCodeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SP_itemCode.setAdapter(adapter);
    }

    //method to display the details of the stock item in TextView
    public void displayDetails () {
        int position = SP_itemCode.getSelectedItemPosition();
        String details = stockList_ArrayList.get(position).toString();
        TV_details.setText(details);
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
