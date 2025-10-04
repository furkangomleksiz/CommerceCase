package com.commercecase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrichedProduct {
    private String name;
    private double popularityScore;
    private double weight;
    private ProductImages images;
    private double price;
    private double rating;
}

