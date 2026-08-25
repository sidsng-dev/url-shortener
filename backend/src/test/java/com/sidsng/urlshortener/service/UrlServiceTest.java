package com.sidsng.urlshortener.service;

import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    private UrlService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        urlService = new UrlService(urlRepository);
    }

    @Test
    void shouldShortenValidUrl() {

        String originalUrl = "https://www.google.com";

        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlService.shortenUrl(
                originalUrl,
                null,
                null
        );

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertEquals(6, result.getShortCode().length());

        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void shouldRejectInvalidUrl() {

        String invalidUrl = "not-a-valid-url";

        assertThrows(
                IllegalArgumentException.class,
                () -> urlService.shortenUrl(
                        invalidUrl,
                        null,
                        null
                )
        );

        verify(urlRepository, never())
                .save(any(Url.class));
    }

    @Test
    void shouldCreateCustomAlias() {

        String originalUrl = "https://github.com";
        String customAlias = "github";

        when(urlRepository.existsByShortCode(customAlias))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlService.shortenUrl(
                originalUrl,
                null,
                customAlias
        );

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(customAlias, result.getShortCode());

        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void shouldRejectDuplicateCustomAlias() {

        String customAlias = "github";

        when(urlRepository.existsByShortCode(customAlias))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> urlService.shortenUrl(
                        "https://github.com",
                        null,
                        customAlias
                )
        );

        verify(urlRepository, never())
                .save(any(Url.class));
    }

    @Test
    void shouldFindUrlByShortCode() {

        Url url = new Url(
                "https://github.com",
                "github"
        );

        when(urlRepository.findByShortCode("github"))
                .thenReturn(Optional.of(url));

        Optional<Url> result =
                urlService.getUrlByShortCode("github");

        assertTrue(result.isPresent());
        assertEquals(
                "https://github.com",
                result.get().getOriginalUrl()
        );

        verify(urlRepository)
                .findByShortCode("github");
    }
}