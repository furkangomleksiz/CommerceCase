package com.commercecase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoldPriceResponse {
    private String metal;
    private String currency;
    
    @JsonProperty("price_gram_24k")
    private double priceGram24k;
    
    private String timestamp;
}

