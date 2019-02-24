package com.example.nfc.service;

public class Payment {

    private String product_id;
    private String sum;

    public Payment() {

    }

    public Payment(String product_id, String sum) {
        this.product_id = product_id;
        this.sum = sum;
    }

    public String getProductId() {
        return product_id;
    }
    public void setProductId(String product_id) {
        this.product_id = product_id;
    }

    public String getSum() {
        return sum;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }

}

