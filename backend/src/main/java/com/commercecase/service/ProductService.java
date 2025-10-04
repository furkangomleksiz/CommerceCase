package com.commercecase.service;

import com.commercecase.model.EnrichedProduct;
import com.commercecase.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private GoldPriceService goldPriceService;
    
    @Value("${products.file.path:products.json}")
    private String productsFilePath;
    
    private List<Product> products;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostConstruct
    public void loadProducts() {
        try {
            // Load from classpath (src/main/resources/products.json)
            // This works in both development and production (packaged JAR)
            Resource resource = new ClassPathResource(productsFilePath);
            InputStream inputStream = resource.getInputStream();
            
            products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            System.out.println("Successfully loaded " + products.size() + " products from classpath");
            inputStream.close();
            
        } catch (IOException e) {
            System.err.println("Error loading products from classpath: " + e.getMessage());
            System.err.println("Make sure products.json is in src/main/resources/");
            e.printStackTrace();
            products = new ArrayList<>();
        }
    }
    
    public List<EnrichedProduct> getEnrichedProducts(Double minPrice, Double maxPrice, 
                                                      Double minRating, Double maxRating) {
        double goldPrice = goldPriceService.getGoldPrice();
        
        return products.stream()
            .map(product -> enrichProduct(product, goldPrice))
            .filter(enrichedProduct -> filterByPrice(enrichedProduct, minPrice, maxPrice))
            .filter(enrichedProduct -> filterByRating(enrichedProduct, minRating, maxRating))
            .collect(Collectors.toList());
    }
    
    private EnrichedProduct enrichProduct(Product product, double goldPrice) {
        // Calculate price: (popularityScore + 1) * weight * goldPrice
        double price = (product.getPopularityScore() + 1) * product.getWeight() * goldPrice;
        
        // Convert popularity score (0-1) to 5-point rating
        double rating = Math.round(product.getPopularityScore() * 5 * 10.0) / 10.0;
        
        return new EnrichedProduct(
            product.getName(),
            product.getPopularityScore(),
            product.getWeight(),
            product.getImages(),
            Math.round(price * 100.0) / 100.0, // Round to 2 decimal places
            rating
        );
    }
    
    private boolean filterByPrice(EnrichedProduct product, Double minPrice, Double maxPrice) {
        if (minPrice != null && product.getPrice() < minPrice) {
            return false;
        }
        if (maxPrice != null && product.getPrice() > maxPrice) {
            return false;
        }
        return true;
    }
    
    private boolean filterByRating(EnrichedProduct product, Double minRating, Double maxRating) {
        if (minRating != null && product.getRating() < minRating) {
            return false;
        }
        if (maxRating != null && product.getRating() > maxRating) {
            return false;
        }
        return true;
    }
}

