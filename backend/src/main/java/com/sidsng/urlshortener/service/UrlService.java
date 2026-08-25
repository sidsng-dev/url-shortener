package com.sidsng.urlshortener.service;

import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int SHORT_CODE_LENGTH = 6;

    private final Random random = new Random();

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(
            String originalUrl,
            LocalDateTime expiresAt,
            String customAlias) {

        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        if (expiresAt != null &&
                expiresAt.isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Expiration time must be in the future"
            );
        }

        String shortCode;

        // Use custom alias if provided
        if (customAlias != null && !customAlias.isBlank()) {

            customAlias = customAlias.trim();

            if (!isValidAlias(customAlias)) {
                throw new IllegalArgumentException(
                        "Custom alias must contain only letters, numbers, hyphens, or underscores"
                );
            }

            if (urlRepository.existsByShortCode(customAlias)) {
                throw new IllegalArgumentException(
                        "Custom alias is already in use"
                );
            }

            shortCode = customAlias;

        } else {

            // Generate random short code
            do {
                shortCode = generateShortCode();
            } while (urlRepository.existsByShortCode(shortCode));
        }

        Url url = new Url(originalUrl, shortCode);

        url.setExpiresAt(expiresAt);

        return urlRepository.save(url);
    }

    public Optional<Url> getUrlByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    public void incrementClickCount(Url url) {
        url.incrementClickCount();
        urlRepository.save(url);
    }

    private String generateShortCode() {

        StringBuilder shortCode = new StringBuilder();

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {

            int index = random.nextInt(CHARACTERS.length());

            shortCode.append(CHARACTERS.charAt(index));
        }

        return shortCode.toString();
    }

    private boolean isValidAlias(String alias) {

        return alias.matches("[a-zA-Z0-9_-]{3,30}");
    }

    private boolean isValidUrl(String url) {

        try {

            URI uri = URI.create(url);

            return uri.getScheme() != null
                    && (uri.getScheme().equalsIgnoreCase("http")
                    || uri.getScheme().equalsIgnoreCase("https"))
                    && uri.getHost() != null;

        } catch (IllegalArgumentException e) {

            return false;
        }
    }
}