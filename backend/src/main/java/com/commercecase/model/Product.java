package com.commercecase.model;

import lombok.Data;

@Data
public class Product {
    private String name;
    private double popularityScore;
    private double weight;
    private ProductImages images;
}

