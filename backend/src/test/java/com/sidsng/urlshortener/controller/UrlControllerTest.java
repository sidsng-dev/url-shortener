package com.sidsng.urlshortener.controller;

import com.sidsng.urlshortener.entity.Url;
import com.sidsng.urlshortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    void shouldCreateShortUrl() throws Exception {

        Url url = new Url(
                "https://github.com",
                "github"
        );

        when(urlService.shortenUrl(
                anyString(),
                any(),
                anyString()
        )).thenReturn(url);

        mockMvc.perform(
                post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": "https://github.com",
                                    "customAlias": "github"
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalUrl")
                .value("https://github.com"))
        .andExpect(jsonPath("$.shortCode")
                .value("github"))
        .andExpect(jsonPath("$.shortUrl")
                .value("http://localhost:8080/github"))
        .andExpect(jsonPath("$.clickCount")
                .value(0));
    }

    @Test
    void shouldRejectEmptyUrl() throws Exception {

        mockMvc.perform(
                post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "url": ""
                                }
                                """)
        )
        .andExpect(status().isBadRequest());
    }
}