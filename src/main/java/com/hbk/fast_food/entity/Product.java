package com.hbk.fast_food.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private int available;
    private Timestamp createdAt;
}
