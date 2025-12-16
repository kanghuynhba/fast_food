package com.hbk.fast_food.entity;

import lombok.Data;

@Data
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private int quality;
    private double unitPrice;
    private double subtotal;

    // Constructors

    OrderItem() {}

    OrderItem(int productId, String productName, int quality) {
        this.productId=productId; 
        this.productName=productName;
        this.quality=quality;
    }

    OrderItem(int orderItemId, int productId, String productName, int quality) {
        this.orderItemId=orderItemId;
        this.productId=productId; 
        this.productName=productName;
        this.quality=quality;
    }

    OrderItem(int orderId, int orderItemId, int productId, String productName, 
            int quality, double unitPrice, double subtotal) {
        this.orderId=orderId;
        this.orderItemId=orderItemId;
        this.productId=productId; 
        this.productName=productName;
        this.quality=quality;
        this.unitPrice=unitPrice;
        this.subtotal=subtotal;
    }

}
