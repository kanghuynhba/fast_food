package com.hbk.fast_food.entity;

import lombok.Data;

@Data
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    // Constructors

    public OrderItem() {}

    public OrderItem(int orderId, int productId, String productName, int quantity) {
        this.orderId=orderId;
        this.productId=productId; 
        this.productName=productName;
        this.quantity=quantity;
    }
}
