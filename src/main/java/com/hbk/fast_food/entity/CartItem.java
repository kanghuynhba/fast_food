package com.hbk.fast_food.entity;

import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private int quantity;
    public CartItem(Product product, int quantity) { 
        this.product = product; 
        this.quantity = quantity; 
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
