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

    public OrderItem(int productId, String productName, int quantity) {
        this.productId=productId; 
        this.productName=productName;
        this.quantity=quantity;
    }

    public OrderItem(int orderItemId, int productId, String productName, int quantity) {
        this.orderItemId=orderItemId;
        this.productId=productId; 
        this.productName=productName;
        this.quantity=quantity;
    }

    public OrderItem(int orderId, int orderItemId, int productId, String productName, 
            int quantity, double unitPrice, double subtotal) {
        this.orderId=orderId;
        this.orderItemId=orderItemId;
        this.productId=productId; 
        this.productName=productName;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
        this.subtotal=subtotal;
    }

}
