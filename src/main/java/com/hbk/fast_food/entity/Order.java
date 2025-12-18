package com.hbk.fast_food.entity;

import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class Order {
    private int orderId;
    private String customerName="";
    private String phoneNumber="";
    private double totalAmount;
    private int status;  //  0: PREPARING, 1: READY, 2: CANCELLED
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItem> items=new ArrayList<>();
   
    public String getStatusName() {
        return switch (status) {
            case 0 -> "PREPARING";
            case 1 -> "READY";
            case 2 -> "CANCELLED";
            default -> "UNKNOWN";
        };
    }

    public boolean isCompleted() {
        return status==2;
    }
}
