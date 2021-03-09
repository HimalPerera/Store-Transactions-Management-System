package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.concurrent.ExecutionException;

public class Sell extends AppCompatActivity {

    //declaring variables
    EditText ET_amount;
    Spinner SP_itemCode;
    TextView TV_transactionID, TV_totalPrice, TV_displayDetails;
    Button BTN_sell;

    JSONArray stockList_JSONArray;
    ArrayList<Stock_Item> stockList_ArrayList = new ArrayList();
    String[] itemCodeList = null;
    String transactionID = null;
    double totalPrice = 0;
    double givenDiscount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable keyboard display when activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.sell);


        //instantiating variables
        TV_transactionID = (TextView) findViewById(R.id.textView_sellingTransactionID);
        ET_amount = (EditText) findViewById(R.id.editText_sellAmount);
        TV_totalPrice = (TextView) findViewById(R.id.textView_totalPriceSelling);
        TV_displayDetails = (TextView) findViewById(R.id.textView_displayDetails_Selling);

        SP_itemCode = (Spinner) findViewById(R.id.spinner_itemCode_Sell);

        BTN_sell = (Button) findViewById(R.id.button_sell);

        //disabling Sell button at the start
        BTN_sell.setEnabled(false);
        BTN_sell.setAlpha(0.2f);


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


        //setting a listener to amount text field to disable Sell button if the input is again changed by the user
        ET_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                BTN_sell.setEnabled(false);
                BTN_sell.setAlpha(0.2f);
            }
        });


        //first of all call loadData method to retrieve data from Database through PHP file
        loadData();
    }


    //method to display total price
    public void displayTotalPrice(View aView) {
        //checking whether amount field is empty
        if (TextUtils.isEmpty(ET_amount.getText())) {
            ET_amount.setError("Enter the Amount");
        } else {
            calculateTotalPrice();
            TV_totalPrice.setText(String.valueOf(this.totalPrice) + " (Rs.)");
            //activating Sell button after the Total button is clicked
            BTN_sell.setEnabled(true);
            BTN_sell.setAlpha(1);
        }

    }


    //method to do the purchasing transaction
    public void onClickSell(View aView) {

        //checking whether all input fields are filled
        if (isInputTextFieldsEmpty()) {
            return;
        } else if (!isInternetConnected()) {
            //display an alert dialog box to inform no internet connection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet");
            builder.setMessage("Internet connection is required");
            builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BTN_sell.performClick();
                }
            });
            builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), Home_Page.class));
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {

            if (checkAvailability() == true) {
                //inserting details into sales database
                insertIntoSales();

                //updating current stock of the sold item  in stock database
                updateCurrentStockInStockDB();


                //display an alert dialog box to do more transactions
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to do more sales transactions ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Sell.class));
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Failed");
                builder.setMessage("Insufficient current stock of the selected item.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
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


    //method to calculate total price
    public void calculateTotalPrice() {
        int amount = Integer.parseInt(ET_amount.getText().toString().trim());
        double unitPrice = stockList_ArrayList.get(SP_itemCode.getSelectedItemPosition()).getUnitPrice();
        double discountRate = stockList_ArrayList.get(SP_itemCode.getSelectedItemPosition()).getDiscountRate();
        int discountLevel = stockList_ArrayList.get(SP_itemCode.getSelectedItemPosition()).getDiscountLevel();

        if (amount >= discountLevel) {
            this.totalPrice = (unitPrice * amount) - ((unitPrice * (discountRate / 100)) * amount);
            double discount = ((unitPrice * (discountRate / 100)) * amount);
            this.givenDiscount = discount;
            Toast.makeText(getApplicationContext(), "Discount applied : " + String.valueOf(discount) + " (Rs.)", Toast.LENGTH_SHORT).show();
        } else {
            this.totalPrice = unitPrice * amount;
            Toast.makeText(getApplicationContext(), "No discount applied : Discount level is " + String.valueOf(discountLevel), Toast.LENGTH_SHORT).show();
        }


    }


    //method to get all stock items from database and load them into arrays and show in the spinner
    public void loadData() {

        Get_All_Stock_Items ob = new Get_All_Stock_Items(getApplicationContext());
        stockList_JSONArray = ob.get_All_Items();

        if (stockList_JSONArray == null) {
            //display an alert dialog box to inform database is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Failed");
            builder.setMessage("Stock Database is empty or Connection Problem");
            builder.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Home_Page.class));

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
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemCodeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SP_itemCode.setAdapter(adapter);


        //load existing sales to get transaction IDs
        //getting data through Volley library
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String phpURL = "http://storetransactions.atwebpages.com/Get_All_Sales.php";

        StringRequest stringRequest = new StringRequest(phpURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray allSales_JSONArray = new JSONArray(response);
                    transactionID = "S-" + (allSales_JSONArray.length() + 1);//adding "S" to inform this is a Selling Transaction

                    //diplaying new transaction ID in text view
                    TV_transactionID.setText("Transaction ID : " + transactionID);

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
                startActivity(new Intent(getApplicationContext(), Sell.class));
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


    //method to display the details of the stock item in TextView
    public void displayDetails() {
        int position = SP_itemCode.getSelectedItemPosition();
        String details = stockList_ArrayList.get(position).toString();
        TV_displayDetails.setText(details);
    }


    //method to insert data into sales database
    public void insertIntoSales() {
        //getting required data
        String transactionID = this.transactionID;
        String itemCode = SP_itemCode.getSelectedItem().toString();
        int amount = Integer.parseInt(ET_amount.getText().toString().trim());
        double totalPrice = this.totalPrice;
        double givenDiscount = this.givenDiscount;

        //creating a object of Selling_Transaction
        Selling_Transaction selling_transaction = new Selling_Transaction(transactionID, itemCode, amount, totalPrice, givenDiscount);

        //defining type of method for Background_Worker class
        String type = "INSERT_SELLING_TRANSACTION";

        //passing the type and the created object to a background worker object
        Background_Worker background_worker = new Background_Worker();
        background_worker.execute(type, selling_transaction);

        //display the result coming from PHP file
        try {
            Toast.makeText(getApplicationContext(), background_worker.get(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    //method to update current stock in stock database
    public void updateCurrentStockInStockDB() {
        //getting required data
        String itemCode = SP_itemCode.getSelectedItem().toString();
        int new_currentStock = getNewCurrentStock();

        //defining type of method for Background_Worker class
        String type = "UPDATE_CURRENT_STOCK";

        //passing the type and the created object to a background worker object
        Background_Worker background_worker = new Background_Worker();
        background_worker.execute(type, itemCode, new_currentStock);

        //display the result coming from PHP file
        try {
            Toast.makeText(getApplicationContext(), background_worker.get(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    //method to calculate the new current stock and return it
    public int getNewCurrentStock() {
        int currentStock = stockList_ArrayList.get(SP_itemCode.getSelectedItemPosition()).getCurrentStock();
        int newCurrentStock = currentStock - Integer.parseInt(ET_amount.getText().toString().trim());

        return newCurrentStock;

    }

    //method to check whether the required amount is available in stock
    public boolean checkAvailability() {
        int amount = Integer.parseInt(ET_amount.getText().toString().trim());
        int currentStock = stockList_ArrayList.get(SP_itemCode.getSelectedItemPosition()).getCurrentStock();

        if (currentStock >= amount) {
            return true;
        } else {
            return false;
        }

    }

    //method to check whether input text fields are not empty
    public boolean isInputTextFieldsEmpty() {
        boolean isEmpty = false;

        if (TextUtils.isEmpty(ET_amount.getText())) {
            ET_amount.setError("Enter the Amount");
            isEmpty = true;
        }

        return isEmpty;
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
