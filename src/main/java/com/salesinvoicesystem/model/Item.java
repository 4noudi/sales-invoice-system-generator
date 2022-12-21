package com.salesinvoicesystem.model;

public class Item {

    private int invoiceNum;
    private String itemName;
    private double itemPrice;
    private int itemCount;
    private Double itemTotal;

    public Item() {
    }

    public Item(int invoiceNum, String itemName, double itemPrice, int itemCount) {
        this.invoiceNum = invoiceNum;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
    }

    public Item(int invoiceNum, String itemName, double itemPrice, int itemCount, Double itemTotal) {
        this.invoiceNum = invoiceNum;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.itemTotal = itemTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public Double getItemTotal() {
        return itemPrice * itemCount;
    }

    public void setItemTotal(Double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Integer getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(Integer invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    @Override
    public String toString() {
        return "Item{" + "itemName=" + itemName + ", itemPrice=" + itemPrice + ", itemCount=" + itemCount + ", itemTotal=" + itemTotal + ", invoiceNum=" + invoiceNum + '}';
    }

    public String getAsCSV() {
        return invoiceNum + "," + itemName + "," + itemPrice + "," + itemCount + "," + itemTotal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return this.getItemName() == ((Item) obj).getItemName();
    }
}
