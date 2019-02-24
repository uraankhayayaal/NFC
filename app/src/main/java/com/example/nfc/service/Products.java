package com.example.nfc.service;

import java.util.List;

public class Products {
    private List<Product> products;

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public Product getFirst()
    {
        return products.get(0);
    }

}
