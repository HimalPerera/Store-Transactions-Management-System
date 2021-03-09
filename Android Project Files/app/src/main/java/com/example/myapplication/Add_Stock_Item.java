package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Add_Stock_Item extends AppCompatActivity {

    //declaring variables
    EditText ET_itemCode, ET_itemName, ET_unitPrice, ET_discountRate, ET_discountLevel;
    Button Btn_AddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable keyboard display when activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.add_stock_item);

        //instantiating edit text variables
        ET_itemCode = (EditText) findViewById(R.id.editText_itemCode);
        ET_itemName = (EditText) findViewById(R.id.editText_itemName);
        ET_unitPrice = (EditText) findViewById(R.id.editText_unitPrice);
        ET_discountRate = (EditText) findViewById(R.id.editText_discountRate);
        ET_discountLevel = (EditText) findViewById(R.id.editText_discountLevel);
        Btn_AddItem = (Button) findViewById(R.id.button_addItem);

    }


    //insert item into the database
    public void AddItem(View aView) {

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
                    Btn_AddItem.performClick();
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

            //checking whether itemCode already exists
            if (isItemCodeAlreadyExists()) {
                return;
            }

            //defining type of method for Background_Worker class
            String type = "ADD_STOCK_ITEM";


            //assigning input values to a Stock_Item variable
            String itemCode = ET_itemCode.getText().toString();
            String itemName = ET_itemName.getText().toString();
            double unitPrice = Double.parseDouble(ET_unitPrice.getText().toString());
            double discountRate = Double.parseDouble(ET_discountRate.getText().toString());
            int discountLevel = Integer.parseInt(ET_discountLevel.getText().toString());

            Stock_Item stock_item = new Stock_Item(itemCode, itemName, unitPrice, discountRate, discountLevel);


            //passing the type and stock item to a Background_Worker
            Background_Worker background_worker = new Background_Worker();
            background_worker.execute(type, stock_item);

            try {
                Toast.makeText(getApplicationContext(), background_worker.get(), Toast.LENGTH_LONG).show();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }


            //start a new activity of Add_More_Stock_Items
            startActivity(new Intent(this, Add_More_Stock_Items.class));
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


    //method to check whether input text fields are not empty
    public boolean isInputTextFieldsEmpty() {
        boolean isEmpty = false;
        if (TextUtils.isEmpty(ET_itemCode.getText())) {
            ET_itemCode.setError("Enter the Item Code");
            isEmpty = true;
        }
        if (TextUtils.isEmpty(ET_itemName.getText())) {
            ET_itemName.setError("Enter the Item Name");
            isEmpty = true;
        }
        if (TextUtils.isEmpty(ET_unitPrice.getText())) {
            ET_unitPrice.setError("Enter the Unit Price");
            isEmpty = true;
        }
        if (TextUtils.isEmpty(ET_discountRate.getText())) {
            ET_discountRate.setError("Enter the Discount Rate");
            isEmpty = true;
        }
        if (TextUtils.isEmpty(ET_discountLevel.getText())) {
            ET_discountLevel.setError("Enter the Discount Level");
            isEmpty = true;
        }

        return isEmpty;
    }


    //method to check itemCode already exists
    public boolean isItemCodeAlreadyExists() {
        boolean isExists = false;

        Get_All_Stock_Items ob = new Get_All_Stock_Items(getApplicationContext());
        JSONArray allStock_JSONArray = ob.get_All_Items();

        //defining the length for String array which stores existing itemCodes
        String[] ItemCodeList_Array = new String[allStock_JSONArray.length()];


        //inserting data into the arrayList from the JSONArray
        for (int i = 0; i < allStock_JSONArray.length(); i++) {
            JSONObject aStockItem = null;

            try {
                aStockItem = allStock_JSONArray.getJSONObject(i);

                String itemCode = aStockItem.getString("itemCode");

                //inserting itemCodes into the String array
                ItemCodeList_Array[i] = itemCode;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < ItemCodeList_Array.length; i++)
        if (ET_itemCode.getText().toString().trim().equals(ItemCodeList_Array [i])) {
            ET_itemCode.setError("Item Code Already Exists");
            isExists = true;
        }

        return isExists;
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