package com.example.myapplication;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


//a general class to get all stock items from database
public class Get_All_Stock_Items {

    //defining variables
    String phpURL = "http://storetransactions.atwebpages.com/Get_All_Stock_Items.php";
    JSONArray allStockItems = null;
    Context context;


    //parameterized constructor
    public Get_All_Stock_Items(Context context) {
        this.context = context;
    }

    //method to get all items
    public JSONArray get_All_Items() {

        //an AsyncTask to retrieve data as a JSONArray
        class GetItems extends AsyncTask<Void, Void, JSONArray> {

            //a string variable to store output string of PHP file which is in JSONArray format
            String jsonArrayString = "";

            @Override
            protected JSONArray doInBackground(Void... voids) {

                try {
                    URL url = new URL(phpURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        jsonArrayString += line + "\n";
                    }


                    inputStream.close();
                    bufferedReader.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //creating a JSONArray from the output string of PHP file
                JSONArray temp = null;
                try {
                    temp = new JSONArray(jsonArrayString.trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //returning the created JSONArray
                return temp;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONArray ja) {
                super.onPostExecute(ja);
            }
        }

        //creating an instance of GetItems class and executing the AsyncTask
        GetItems ob = new GetItems();
        ob.execute();


        //assigning the output of the AsyncTask to the defined JSONArray variable
        try {
            allStockItems = ob.get(); //getting the output from the AsyncTask
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //returning the JSONArray variable from the method
        return allStockItems;
    }


}
