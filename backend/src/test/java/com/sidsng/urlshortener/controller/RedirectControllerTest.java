package com.sidsng.urlshortener.controller;

import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RedirectControllerTest {

    @Mock
    private UrlService urlService;

    private RedirectController redirectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redirectController = new RedirectController(urlService);
    }

    @Test
    void shouldRedirectToOriginalUrl() {

        Url url = new Url(
                "https://github.com",
                "github"
        );

        when(urlService.getUrlByShortCode("github"))
                .thenReturn(Optional.of(url));

        ResponseEntity<Void> response =
                redirectController.redirectUrl("github");

        assertEquals(
                302,
                response.getStatusCode().value()
        );

        assertEquals(
                "https://github.com",
                response.getHeaders().getFirst("Location")
        );

        verify(urlService)
                .getUrlByShortCode("github");

        verify(urlService)
                .incrementClickCount(url);
    }

    @Test
    void shouldReturnNotFoundWhenShortCodeDoesNotExist() {

        when(urlService.getUrlByShortCode("unknown"))
                .thenReturn(Optional.empty());

        ResponseEntity<Void> response =
                redirectController.redirectUrl("unknown");

        assertEquals(
                404,
                response.getStatusCode().value()
        );

        verify(urlService)
                .getUrlByShortCode("unknown");

        verify(urlService, never())
                .incrementClickCount(any());
    }

    @Test
    void shouldReturnNotFoundWhenUrlIsExpired() {

        Url url = new Url(
                "https://github.com",
                "github"
        );

        url.setExpiresAt(
                LocalDateTime.now().minusMinutes(10)
        );

        when(urlService.getUrlByShortCode("github"))
                .thenReturn(Optional.of(url));

        ResponseEntity<Void> response =
                redirectController.redirectUrl("github");

        assertEquals(
                404,
                response.getStatusCode().value()
        );

        verify(urlService)
                .getUrlByShortCode("github");

        verify(urlService, never())
                .incrementClickCount(any());
    }
}