package com.example.nfc.service;

public class Product {
    private Integer id;
    private String alias;
    private String accountNumber;
    private String currency;
    private String balance;

    public Product() {

    }

    public Product(Integer id, String alias, String accountNumber, String currency, String balance) {
        this.id = id;
        this.alias = alias;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHistory() {
        return "History";
    }


}

