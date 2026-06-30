package com.hotelmanagementsystem.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * Key resolvers for Spring Cloud Gateway's RequestRateLimiter filter.
 *
 * Two strategies:
 *  - ipKeyResolver   : bucket per client IP address (used on public/auth routes)
 *  - userKeyResolver : bucket per authenticated user (from X-Auth-Username header)
 *                      Falls back to IP if the header is absent.
 *
 * These beans are referenced in application.yml:
 *   key-resolver: "#{@ipKeyResolver}"
 *   key-resolver: "#{@userKeyResolver}"
 */
@Configuration
public class RateLimiterConfig {

    /**
     * Rate-limit by client IP address.
     * Best for public endpoints (login, register) where there is no user context yet.
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            // Respect X-Forwarded-For so it works behind a load balancer / reverse proxy
            String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return Mono.just(xForwardedFor.split(",")[0].trim());
            }
            String remoteAddress = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
            return Mono.just(remoteAddress);
        };
    }

    /**
     * Rate-limit by authenticated username.
     * The JwtAuthenticationFilter sets X-Auth-Username before this runs.
     * Falls back to IP for unauthenticated calls that slip through.
     */
    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String username = exchange.getRequest().getHeaders().getFirst("X-Auth-Username");
            if (username != null && !username.isEmpty()) {
                return Mono.just("user:" + username);
            }
            // Fallback to IP
            String ip = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
            return Mono.just("ip:" + ip);
        };
    }
}