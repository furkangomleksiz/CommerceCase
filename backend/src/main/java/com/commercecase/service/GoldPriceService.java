package com.commercecase.service;

import com.commercecase.model.GoldPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoldPriceService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoldPriceService.class);
    
    @Value("${goldapi.url}")
    private String goldApiUrl;
    
    @Value("${goldapi.token}")
    private String goldApiToken;
    
    @Value("${goldapi.cache.duration}")
    private long cacheDuration;
    
    private Double cachedGoldPrice;
    private long lastFetchTime = 0;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public double getGoldPrice() {
        long currentTime = System.currentTimeMillis();
        
        // Return cached price if still valid
        if (cachedGoldPrice != null && (currentTime - lastFetchTime) < cacheDuration) {
            long cacheAgeSeconds = (currentTime - lastFetchTime) / 1000;
            logger.info("Returning CACHED gold price: ${}/gram (cache age: {} seconds)", cachedGoldPrice, cacheAgeSeconds);
            return cachedGoldPrice;
        }
        
        try {
            // Set up headers with API token
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-access-token", goldApiToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            logger.info("Fetching fresh gold price from API...");
            
            // Fetch gold price
            GoldPriceResponse response = restTemplate.exchange(
                goldApiUrl,
                HttpMethod.GET,
                entity,
                GoldPriceResponse.class
            ).getBody();
            
            if (response != null && response.getPriceGram24k() > 0) {
                cachedGoldPrice = response.getPriceGram24k();
                lastFetchTime = currentTime;
                logger.info("Successfully fetched FRESH gold price from API: ${}/gram (cached for {} seconds)", 
                    cachedGoldPrice, cacheDuration / 1000);
                return cachedGoldPrice;
            }
        } catch (Exception e) {
            logger.error("Error fetching gold price from API: {}", e.getMessage());
            // Return fallback price if API fails
            if (cachedGoldPrice != null) {
                long cacheAgeSeconds = (currentTime - lastFetchTime) / 1000;
                logger.warn("Using EXPIRED CACHE as fallback: ${}/gram (cache age: {} seconds)", 
                    cachedGoldPrice, cacheAgeSeconds);
                return cachedGoldPrice;
            }
        }
        
        // Fallback price (approximate current gold price per gram in USD)
        logger.warn("Using HARDCODED FALLBACK gold price: $65.0/gram (API unavailable, no cache available)");
        return 65.0;
    }
}

