package com.example.accommodationbookingservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    private String key;
    private String successUrl;
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = key;
    }
}