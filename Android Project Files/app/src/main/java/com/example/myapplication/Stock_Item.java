package com.example.myapplication;

public class Stock_Item {

    //declaring variables
    private String itemCode = "";
    private String itemName = "";
    private double unitPrice = 0;
    private double discountRate = 0;
    private int discountLevel = 0;
    private int currentStock = 0;


    //default constructor
    public Stock_Item() {
    }


    //parameterized constructors
    public Stock_Item(String itemCode, String itemName, double unitPrice, double discountRate, int discountLevel) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.discountLevel = discountLevel;
    }

    public Stock_Item(String itemCode, String itemName, double unitPrice, double discountRate, int discountLevel, int currentStock) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.discountLevel = discountLevel;
        this.currentStock = currentStock;
    }

    //getters and setters
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getDiscountLevel() {
        return discountLevel;
    }

    public void setDiscountLevel(int discountLevel) {
        this.discountLevel = discountLevel;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }


    //toString
    @Override
    public String toString() {
        return  "Item Code : " + itemCode + '\n' +
                "Item Name : " + itemName + '\n' +
                "Unit Price : " + unitPrice + " (Rs.)" +'\n' +
                "Discount Rate : " + discountRate + "%" +'\n' +
                "Discount Level : " + discountLevel +'\n' +
                "Current Stock : " + currentStock;
    }
}
