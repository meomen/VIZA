package com.vuducminh.viza.models;

public class History {
    private int typeOrder;
    private int price;
    private String DateCreated;
    private String DateFormat;

    public History() {
    }

    public History(int typeOrder, int price, String dateCreated, String dateFormat) {
        this.typeOrder = typeOrder;
        this.price = price;
        DateCreated = dateCreated;
        DateFormat = dateFormat;
    }

    public int getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getDateFormat() {
        return DateFormat;
    }

    public void setDateFormat(String dateFormat) {
        DateFormat = dateFormat;
    }
}
