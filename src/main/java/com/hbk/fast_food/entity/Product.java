package com.hbk.fast_food.entity;

import lombok.Data;

@Data
public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
}
