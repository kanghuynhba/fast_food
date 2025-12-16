package com.hbk.fast_food.entity;

import lombok.Data;

@Data
public class Cashier {
    private int cashierId;
    private String username;
    private String password;
    private int status;

    public boolean isActive() {
        return status==1;
    }
}
