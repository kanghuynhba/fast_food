package com.hbk.fast_food.entity;

import lombok.Data;
import java.util.*;

@Data
public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private int status;  // 0: Unpaid, 1: Paid, 2: Refunded, 3: Failed
    private String notes;
    private Date createdAt;

    public boolean isSuccess() {
        return status==1;
    }
}
