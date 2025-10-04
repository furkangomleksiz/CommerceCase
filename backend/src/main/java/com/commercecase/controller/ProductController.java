package com.commercecase.controller;

import com.commercecase.model.EnrichedProduct;
import com.commercecase.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/products")
    public List<EnrichedProduct> getProducts(
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Double minRating,
        @RequestParam(required = false) Double maxRating
    ) {
        return productService.getEnrichedProducts(minPrice, maxPrice, minRating, maxRating);
    }
}

