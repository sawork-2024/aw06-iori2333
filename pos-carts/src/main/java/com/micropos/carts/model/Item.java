package com.micropos.carts.model;

import java.io.Serializable;

public class Item implements Serializable {
    private final String productId;
    private int quantity;

    public Item(String product, int quantity) {
        this.productId = product;
        this.quantity = quantity;
    }

    public String getProduct() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Item{" + "productId=" + productId + ", quantity=" + quantity + '}';
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
