package com.sidsng.urlshortener.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static class RequestInfo {

        private int requestCount;
        private long windowStart;

        public RequestInfo() {
            this.requestCount = 0;
            this.windowStart = Instant.now().getEpochSecond();
        }
    }

    private final Map<String, RequestInfo> clients =
            new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);

        RequestInfo requestInfo = clients.computeIfAbsent(
                clientIp,
                key -> new RequestInfo()
        );

        synchronized (requestInfo) {

            long currentTime = Instant.now().getEpochSecond();

            // Reset the counter after 60 seconds
            if (currentTime - requestInfo.windowStart >= 60) {
                requestInfo.requestCount = 0;
                requestInfo.windowStart = currentTime;
            }

            requestInfo.requestCount++;

            // Check rate limit
            if (requestInfo.requestCount >
                    RateLimitConfig.MAX_REQUESTS_PER_MINUTE) {

                response.setStatus(429);
                response.setContentType("application/json");

                response.getWriter().write("""
                        {
                            "status": 429,
                            "error": "Too Many Requests",
                            "message": "Rate limit exceeded. Try again later."
                        }
                        """);

                return;
            }
        }

        // Continue with the request
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {

        String forwardedFor =
                request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}