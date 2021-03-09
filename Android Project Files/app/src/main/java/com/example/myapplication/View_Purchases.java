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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View_Purchases extends AppCompatActivity {
    //declaring variables
    ListView LV_displayPurchases;
    TextView TV_noOfTransactions, TV_grandTotal;
    JSONArray allPurchases_JSONArray = null;
    ArrayList<Purchasing_Transaction> allPurchases_ArrayList = new ArrayList<Purchasing_Transaction>();
    String[] PurchasesList_Array = null;
    double grandTotal = 0;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_purchases);


        //instantiating variables
        LV_displayPurchases = (ListView) findViewById(R.id.listView_displayPurchases);
        TV_noOfTransactions = (TextView) findViewById(R.id.textView_noOfTransactions_Purchases);
        TV_grandTotal = (TextView) findViewById(R.id.textView_grandTotal_Purchases);


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
        LV_displayPurchases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = allPurchases_ArrayList.get(position).toString();
                Toast.makeText(getApplicationContext(), details, Toast.LENGTH_LONG).show();
            }
        });


        //getting all purchases from purchases database, load them into arrays and display them
        getAllPurchases_and_LoadInToArrays_and_Display();

    }

    //method to get purchases from database and load in to arrays and display them
    public void getAllPurchases_and_LoadInToArrays_and_Display() {

        //getting data through Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String phpURL = "http://storetransactions.atwebpages.com/Get_All_Purchases.php";

        StringRequest stringRequest = new StringRequest(phpURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    allPurchases_JSONArray = new JSONArray(response);


                    if (allPurchases_JSONArray == null) {
                        //display an alert dialog box to inform database is empty
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Failed");
                        builder.setMessage("Purchases Database is empty");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), Home_Page.class));
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                    }


                    loadInToArrays();

                    displayDetails();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error.toString());
            }
        });


        //sending the request
        requestQueue.add(stringRequest);
    }


    public void onError(String error) {
        //display an alert dialog box to inform an error occurred
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed");
        builder.setMessage(error);
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(), View_Purchases.class));
            }
        });
        builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(), Home_Page.class));

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //method to load purchasing details in to arrays
    public void loadInToArrays() {
        //defining the length for String array which is the list of ListView
        PurchasesList_Array = new String[allPurchases_JSONArray.length()];


        //inserting data into the arrayList from the JSONArray
        for (int i = 0; i < allPurchases_JSONArray.length(); i++) {
            JSONObject aPurchase = null;
            Purchasing_Transaction purchasing_transaction = null;

            try {
                aPurchase = allPurchases_JSONArray.getJSONObject(i);

                String transactionID = aPurchase.getString("transaction_ID");
                String itemCode = aPurchase.getString("itemCode");
                int amount = aPurchase.getInt("amount");
                double totalPrice = aPurchase.getDouble("totalPrice");

                //inserting transactionID and totalPrice into the String array for the labels of ListView
                PurchasesList_Array[i] = "Transaction ID : " + transactionID + "\n" + "Total Price : " + totalPrice + " (Rs.)";

                //creating a new Purchasing_Transaction and adding it to the arrayList
                purchasing_transaction = new Purchasing_Transaction(transactionID, itemCode, amount, totalPrice);
                allPurchases_ArrayList.add(purchasing_transaction);
                grandTotal += totalPrice;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //method to dispalay the details in the list view
    public void displayDetails() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PurchasesList_Array);
        LV_displayPurchases.setAdapter(arrayAdapter);

        TV_noOfTransactions.setText("No. of Transactions : " + String.valueOf(allPurchases_ArrayList.size()));
        TV_grandTotal.setText("Grand Total : " + grandTotal + " (Rs.)");
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
