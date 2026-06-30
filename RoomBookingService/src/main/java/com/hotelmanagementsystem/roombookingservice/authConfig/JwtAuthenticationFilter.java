package com.hotelmanagementsystem.roombookingservice.authConfig;
import com.hotelmanagementsystem.roombookingservice.authConfig.AuthServiceClient;
import com.hotelmanagementsystem.roombookingservice.dto.UserValidationResponse;
import com.hotelmanagementsystem.roombookingservice.authConfig.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT processing for public endpoints
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/actuator")
                || path.startsWith("/api/rooms") && request.getMethod().equals("GET")
                || path.startsWith("/api/rooms/availability")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // If no token, let Spring Security handle it (will deny if endpoint requires auth)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Call AuthService to validate token
            UserValidationResponse validationResponse = authServiceClient.validateToken(authHeader);

            if (validationResponse != null && validationResponse.isValid()) {
                // Create UserPrincipal with validated user info
                UserPrincipal userPrincipal = new UserPrincipal(
                        validationResponse.getUsername(),
                        validationResponse.getRole()
                );

                // Create Spring Security authority (must have ROLE_ prefix)
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + validationResponse.getRole());

                // Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userPrincipal,
                                null,
                                Collections.singletonList(authority)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication in Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("User {} authenticated with role {}",
                        validationResponse.getUsername(),
                        validationResponse.getRole());
            } else {
                log.warn("Token validation failed: {}",
                        validationResponse != null ? validationResponse.getMessage() : "null response");
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: ", e);
            // Clear security context on error
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
