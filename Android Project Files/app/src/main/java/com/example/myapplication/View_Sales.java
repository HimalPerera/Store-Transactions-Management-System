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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View_Sales extends AppCompatActivity {

    //declaring variables
    ListView LV_displaySales;
    TextView TV_noOfTransactions, TV_grandTotal;
    JSONArray allSales_JSONArray = null;
    ArrayList<Selling_Transaction> allSales_ArrayList = new ArrayList<Selling_Transaction>();
    String[] SalesList_Array = null;
    double grandTotal = 0;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sales);


        //instantiating variables
        LV_displaySales = (ListView) findViewById(R.id.listView_displaySales);
        TV_noOfTransactions = (TextView) findViewById(R.id.textView_noOfTransactions_Sales);
        TV_grandTotal = (TextView) findViewById(R.id.textView_grandTotal_Sales);


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
        LV_displaySales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = allSales_ArrayList.get(position).toString();
                Toast.makeText(getApplicationContext(), details, Toast.LENGTH_LONG).show();
            }
        });


        //getting all sales from sales database, load them into arrays and display them
        getAllSales_and_LoadInToArrays_and_Display();
    }


    //method to get sales from database and load in to arrays and display them
    public void getAllSales_and_LoadInToArrays_and_Display() {

        //getting data through Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String phpURL = "http://storetransactions.atwebpages.com/Get_All_Sales.php";

        StringRequest stringRequest = new StringRequest(phpURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    allSales_JSONArray = new JSONArray(response);


                    if (allSales_JSONArray == null) {
                        //display an alert dialog box to inform database is empty
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Failed");
                        builder.setMessage("Sales Database is empty");
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
                    //Toast.makeText(getApplicationContext(),String.valueOf(PurchasesList_Array.length()), Toast.LENGTH_LONG).show();
                    displayDetails();
                    //Toast.makeText(getApplicationContext(), String.valueOf(allPurchases_ArrayList.size()), Toast.LENGTH_LONG).show();

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
                startActivity(new Intent(getApplicationContext(), View_Sales.class));
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


    //method to load sales details in to arrays
    public void loadInToArrays() {
        //defining the length for String array which is the list of ListView
        SalesList_Array = new String[allSales_JSONArray.length()];


        //inserting data into the arrayList from the JSONArray
        for (int i = 0; i < allSales_JSONArray.length(); i++) {
            JSONObject aSale = null;
            Selling_Transaction selling_transaction = null;

            try {
                aSale = allSales_JSONArray.getJSONObject(i);

                String transactionID = aSale.getString("transaction_ID");
                String itemCode = aSale.getString("itemCode");
                int amount = aSale.getInt("amount");
                double totalPrice = aSale.getDouble("totalPrice");
                double givenDiscount = aSale.getDouble("givenDiscount");

                //inserting transactionID and totalPrice into the String array for the labels of ListView
                SalesList_Array[i] = "Transaction ID : " + transactionID + "\n" + "Total Price : " + totalPrice + " (Rs.)";

                //creating a new Selling_Transaction and adding it to the arrayList
                selling_transaction = new Selling_Transaction(transactionID, itemCode, amount, totalPrice, givenDiscount);
                allSales_ArrayList.add(selling_transaction);
                grandTotal += totalPrice;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    //method to dispalay the details in the list view
    public void displayDetails() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SalesList_Array);
        LV_displaySales.setAdapter(arrayAdapter);

        TV_noOfTransactions.setText("No. of Transactions : " + String.valueOf(allSales_ArrayList.size()));
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
