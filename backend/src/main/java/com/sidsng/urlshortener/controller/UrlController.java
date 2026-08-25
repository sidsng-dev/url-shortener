package com.sidsng.urlshortener.controller;

import com.sidsng.urlshortener.dto.UrlRequest;
import com.sidsng.urlshortener.dto.UrlResponse;
import com.sidsng.urlshortener.dto.UrlStatsResponse;
import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> shortenUrl(
            @RequestBody UrlRequest request) {

        if (request.getUrl() == null || request.getUrl().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Url savedUrl = urlService.shortenUrl(
                request.getUrl(),
                request.getExpiresAt(),
                request.getCustomAlias()
        );

        UrlResponse response = new UrlResponse(
                savedUrl.getOriginalUrl(),
                savedUrl.getShortCode(),
                "http://localhost:8080/" + savedUrl.getShortCode(),
                savedUrl.getClickCount(),
                savedUrl.getExpiresAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(
            @PathVariable String shortCode) {

        Optional<Url> url = urlService.getUrlByShortCode(shortCode);

        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Url foundUrl = url.get();

        UrlStatsResponse response = new UrlStatsResponse(
                foundUrl.getOriginalUrl(),
                foundUrl.getShortCode(),
                "http://localhost:8080/" + foundUrl.getShortCode(),
                foundUrl.getClickCount(),
                foundUrl.getCreatedAt(),
                foundUrl.getExpiresAt()
        );

        return ResponseEntity.ok(response);
    }
}