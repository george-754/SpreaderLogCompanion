package com.grhprogramming.spreaderlogcompanion;

public class SendDataModel {
    String s_key;
    String s_date;
    String s_farmer;
    String s_field;
    String s_product;
    String s_acres;

    // Initializer
    public SendDataModel() {
        this.s_key = "123456789";
        this.s_date = "5/6/1990";
        this.s_farmer = "N/A";
        this.s_field = "N/A";
        this.s_product = "N/A";
        this.s_acres = "0";
    }

    // Getters
    public String getKey() {
        return s_key;
    }

    public String getDate () {
        return s_date;
    }

    public String getFarmer() {
        return s_farmer;
    }

    public String getField() {
        return s_field;
    }

    public String getProduct() {
        return s_product;
    }

    public String getAcres() {
        return s_acres;
    }

    // Setters
    public void setKey(String sKey) {
        this.s_key = sKey;
    }

    public void setDate(String sDate) {
        this.s_date = sDate;
    }

    public void setFarmer(String sFarmer) {
        this.s_farmer = sFarmer;
    }

    public void setField(String sField) {
        this.s_field = sField;
    }

    public void setProduct(String sProduct) {
        this.s_product = sProduct;
    }

    public void setAcres(String sAcres) {
        this.s_acres = sAcres;
    }

}
