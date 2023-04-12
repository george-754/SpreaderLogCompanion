package com.grhprogramming.spreaderlogcompanion;

public class DataModel {
    String d_id;
    String d_date;
    String d_farmer;
    String d_field;
    String d_product;
    String d_acres;

    public DataModel(String id, String date, String farmer, String field, String product, String acres) {
        this.d_id = id;
        this.d_date = date;
        this.d_farmer = farmer;
        this.d_field = field;
        this.d_product = product;
        this.d_acres = acres;
    }

    public  String getId() { return d_id; }
    public String getDate() {
        return d_date;
    }

    public String getFarmer() {
        return d_farmer;
    }

    public String getField() {
        return d_field;
    }

    public String getProduct() {
        return d_product;
    }

    public String getAcres() {
        return d_acres;
    }
}
