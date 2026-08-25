package com.sidsng.urlshortener.controller;

import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectUrl(
            @PathVariable String shortCode) {

        Optional<Url> url = urlService.getUrlByShortCode(shortCode);

        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Url foundUrl = url.get();

        // Check whether the URL has expired
        if (foundUrl.isExpired()) {
            return ResponseEntity.notFound().build();
        }

        // Count the visit
        urlService.incrementClickCount(foundUrl);

        // Redirect to the original URL
        return ResponseEntity.status(302)
                .header("Location", foundUrl.getOriginalUrl())
                .build();
    }
}