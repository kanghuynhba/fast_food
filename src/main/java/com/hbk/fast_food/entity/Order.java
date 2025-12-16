package com.hbk.fast_food.entity;

import java.util.*;
import lombok.Data;

@Data
public class Order {
    private int orderId;
    private double totalAmount;
    private int paymentStatus;  // 0: Unpaid, 1: Paid, 2: Refunded, 3: Failed
    private int status;  //  0: PREPARING, 1: READY, 2: CANCELLED
    private Date createdAt;
    private Date updatedAt;
    private List<OrderItem> items=new ArrayList<>();
    
    public String getStatusName() {
        return switch (status) {
            case 0 -> "PREPARING";
            case 1 -> "READY";
            case 2 -> "CANCELLED";
            default -> "UNKNOWN";
        };
    }

    public boolean isPaid() {
        return paymentStatus==1;
    }

    public boolean isCompleted() {
        return status==5;
    }
}
