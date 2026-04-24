package com.hotelmanagementsystem.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Per-route logging filter.
 * Logs method, path, query params, downstream service, HTTP status, and elapsed time.
 *
 * Usage in application.yml:
 *   filters:
 *     - name: RequestLoggingFilter
 */
@Component
public class RequestLoggingFilter extends AbstractGatewayFilterFactory<RequestLoggingFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    public RequestLoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            long startTime = System.currentTimeMillis();

            String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
            String path   = request.getPath().value();
            String query  = request.getURI().getRawQuery();
            String clientIp = getClientIp(request);

            log.info("→ Incoming  {} {} {} | client: {}",
                    method, path, query != null ? "?" + query : "", clientIp);

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        long elapsed = System.currentTimeMillis() - startTime;
                        int status = exchange.getResponse().getStatusCode() != null
                                ? exchange.getResponse().getStatusCode().value()
                                : 0;
                        log.info("← Completed {} {} | status: {} | {}ms", method, path, status, elapsed);
                    }));
        };
    }

    private String getClientIp(ServerHttpRequest request) {
        // Check common proxy headers first
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
    }

    public static class Config {
        // No configuration options needed
    }
}