package com.sidsng.urlshortener.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    // Maximum requests allowed in one minute
    public static final int MAX_REQUESTS_PER_MINUTE = 100;
}