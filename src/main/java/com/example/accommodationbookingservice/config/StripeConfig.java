package com.example.accommodationbookingservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StripeConfig {

    private final StripeProperties stripeProperties;

    @PostConstruct
    public void initSecretKey() {
        Stripe.apiKey = stripeProperties.getKey();
    }

    @Bean
    public String successUrl() {
        return stripeProperties.getSuccessUrl();
    }

    @Bean
    public String cancelUrl() {
        return stripeProperties.getCancelUrl();
    }
}