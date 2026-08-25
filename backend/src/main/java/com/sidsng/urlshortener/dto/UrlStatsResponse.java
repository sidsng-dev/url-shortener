package com.sidsng.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {

    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public UrlStatsResponse(
            String originalUrl,
            String shortCode,
            String shortUrl,
            Long clickCount,
            LocalDateTime createdAt,
            LocalDateTime expiresAt) {

        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}