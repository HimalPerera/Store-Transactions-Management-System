package com.example.myapplication;

public class Selling_Transaction {


    //declaring variables
    private String transaction_Id = "";
    private String itemCode = "";
    private int amount = 0;
    private double totalPrice = 0;
    private double givenDiscount = 0;


    //default constructor
    public Selling_Transaction() {
    }


    //parameterized constructor
    public Selling_Transaction(String transaction_Id, String itemCode, int amount, double totalPrice, double givenDiscount) {
        this.transaction_Id = transaction_Id;
        this.itemCode = itemCode;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.givenDiscount = givenDiscount;
    }


    //setters and getters
    public String getTransaction_Id() {
        return transaction_Id;
    }

    public void setTransaction_Id(String transaction_Id) {
        this.transaction_Id = transaction_Id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getGivenDiscount() {
        return givenDiscount;
    }

    public void setGivenDiscount(double givenDiscount) {
        this.givenDiscount = givenDiscount;
    }


    //toString
    @Override
    public String toString() {
        return "Transaction ID : " + transaction_Id + "\n" +
                "Item Code : " + itemCode + "\n" +
                "Amount : " + amount + "\n" +
                "Total Price : " + totalPrice + " (Rs.)" + "\n" +
                "Given Discount : " + givenDiscount + " (Rs.)";
    }
}
