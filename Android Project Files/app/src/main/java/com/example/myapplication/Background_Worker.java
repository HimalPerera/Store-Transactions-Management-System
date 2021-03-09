package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Background_Worker extends AsyncTask<Object, Void, String> {


    //implementing methods
    @Override
    protected String doInBackground(Object... objects) {
        //extracting required data from input parameters
        String type = (String) objects[0];


        //defining the url of PHP file "Add_Stock_Item"
        String url_Add_Stock_Item = "http://storetransactions.atwebpages.com/Add_Stock_Item.php";
        //defining the url of PHP file "Delete_Stock_Item"
        String url_Remove_Stock_Item = "http://storetransactions.atwebpages.com/Delete_Stock_Item.php";
        //defining the url of PHP file "Insert_Purchase_Transaction"
        String url_Insert_Purchase_Transaction = "http://storetransactions.atwebpages.com/Insert_Purchase_Transaction.php";
        //defining the url of PHP file "Update_Current_Stock"
        String url_Update_Current_Stock = "http://storetransactions.atwebpages.com/Update_Current_Stock.php";
        //defining the url of PHP file "Insert_Selling_Transaction"
        String url_Insert_Selling_Transaction = "http://storetransactions.atwebpages.com/Insert_Selling_Transaction.php";


        //identifying the type of operation and performing required database operations
        if (type.equals("ADD_STOCK_ITEM")) {
            //extracting required data from input parameters
            Stock_Item stock_item = (Stock_Item) objects[1];

            try {
                //connecting to database
                URL url = new URL(url_Add_Stock_Item);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                //input data in to PHP file
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //defining the data URL
                String data_url = URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode(stock_item.getItemCode(), "UTF-8") + "&" +
                        URLEncoder.encode("itemName", "UTF-8") + "=" + URLEncoder.encode(stock_item.getItemName(), "UTF-8") + "&" +
                        URLEncoder.encode("unitPrice", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(stock_item.getUnitPrice()), "UTF-8") + "&" +
                        URLEncoder.encode("discountRate", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(stock_item.getDiscountRate()), "UTF-8") + "&" +
                        URLEncoder.encode("discountLevel", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(stock_item.getDiscountLevel()), "UTF-8") + "&" +
                        URLEncoder.encode("currentStock", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");

                //posting data to the PHP file
                bufferedWriter.write(data_url);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //reading the output from the PHP file
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String output = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    output += line + "\n";
                    ;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return output from PHP file
                return output.trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("REMOVE_STOCK_ITEM")) {
            //extracting required data from input parameters
            String item_Code = String.valueOf(objects[1]);
            try {
                //connecting to database
                URL url = new URL(url_Remove_Stock_Item);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                //input data in to PHP file
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //defining the data URL
                String data_url = URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode(item_Code, "UTF-8");

                //posting data to the PHP file
                bufferedWriter.write(data_url);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //reading the output from the PHP file
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String output = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    output += line + "\n";
                    ;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return output from PHP file
                return output.trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("INSERT_PURCHASE_TRANSACTION")) {
            //extracting required data from input parameters
            Purchasing_Transaction purchasing_transaction = (Purchasing_Transaction) objects[1];

            try {
                //connecting to database
                URL url = new URL(url_Insert_Purchase_Transaction);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                //input data in to PHP file
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //defining the data URL
                String data_url = URLEncoder.encode("transaction_ID", "UTF-8") + "=" + URLEncoder.encode(purchasing_transaction.getTransaction_Id(), "UTF-8") + "&" +
                        URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode(purchasing_transaction.getItemCode(), "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(purchasing_transaction.getAmount()), "UTF-8") + "&" +
                        URLEncoder.encode("totalPrice", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(purchasing_transaction.getTotalPrice()), "UTF-8");

                //posting data to the PHP file
                bufferedWriter.write(data_url);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //reading the output from the PHP file
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String output = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    output += line + "\n";
                    ;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return output from PHP file
                return output.trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("UPDATE_CURRENT_STOCK")) {
            //extracting required data from input parameters
            String itemCode = String.valueOf(objects[1]);
            int new_currentStock = (int) objects[2];

            try {
                //connecting to database
                URL url = new URL(url_Update_Current_Stock);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                //input data in to PHP file
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //defining the data URL
                String data_url = URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode(itemCode, "UTF-8") + "&" +
                        URLEncoder.encode("new_currentStock", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(new_currentStock), "UTF-8");

                //posting data to the PHP file
                bufferedWriter.write(data_url);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //reading the output from the PHP file
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String output = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    output += line + "\n";
                    ;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return output from PHP file
                return output.trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("INSERT_SELLING_TRANSACTION")) {
            //extracting required data from input parameters
            Selling_Transaction selling_transaction = (Selling_Transaction) objects[1];

            try {
                //connecting to database
                URL url = new URL(url_Insert_Selling_Transaction);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                //input data in to PHP file
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //defining the data URL
                String data_url = URLEncoder.encode("transaction_ID", "UTF-8") + "=" + URLEncoder.encode(selling_transaction.getTransaction_Id(), "UTF-8") + "&" +
                        URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode(selling_transaction.getItemCode(), "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(selling_transaction.getAmount()), "UTF-8") + "&" +
                        URLEncoder.encode("totalPrice", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(selling_transaction.getTotalPrice()), "UTF-8") + "&" +
                        URLEncoder.encode("givenDiscount", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(selling_transaction.getGivenDiscount()), "UTF-8");

                //posting data to the PHP file
                bufferedWriter.write(data_url);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //reading the output from the PHP file
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String output = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    output += line + "\n";
                    ;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //return output from PHP file
                return output.trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String output) {
        super.onPostExecute(output);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
