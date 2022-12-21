package com.salesinvoicesystem.model;

import java.util.ArrayList;

public class Invoice {

    private Integer invoiceNumer;
    private String invoiceDate;
    private String invoiceCustomerName;
    private ArrayList<Item> items;

    public Invoice() {
    }

    public Invoice(Integer invoiceNumer, String invoiceDate, String invoiceCustomerName) {
        this.invoiceNumer = invoiceNumer;
        this.invoiceDate = invoiceDate;
        this.invoiceCustomerName = invoiceCustomerName;
    }

    public double getInvoiceTotal() {
        double total = 0.0;
        for (Item item : getItems()) {
            total += item.getItemTotal();
        }
        return total;
    }

    public ArrayList<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public String getCustomer() {
        return invoiceCustomerName;
    }

    public void setCustomer(String customer) {
        this.invoiceCustomerName = customer;
    }

    public Integer getInvoiceNumer() {
        return invoiceNumer;
    }

    public void setInvoiceNumer(Integer invoiceNumer) {
        this.invoiceNumer = invoiceNumer;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceCustomerName() {
        return invoiceCustomerName;
    }

    public void setInvoiceCustomerName(String invoiceCustomerName) {
        this.invoiceCustomerName = invoiceCustomerName;
    }

    @Override
    public String toString() {
        return "Invoice{" + "invoiceNumer=" + invoiceNumer + ", invoiceDate=" + invoiceDate + ", invoiceCustomerName=" + invoiceCustomerName + "," + " items=" + items + '}';
    }

    public String getAsCSV() {
        return invoiceNumer + "," + invoiceDate + "," + invoiceCustomerName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return this.getInvoiceNumer() == ((Invoice) obj).getInvoiceNumer();
    }
}
