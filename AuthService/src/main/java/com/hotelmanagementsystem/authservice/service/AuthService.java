package com.hotelmanagementsystem.authservice.service;

import com.hotelmanagementsystem.authservice.dto.*;
import com.hotelmanagementsystem.authservice.entity.RefreshToken;
import com.hotelmanagementsystem.authservice.entity.UserRole;
import com.hotelmanagementsystem.authservice.entity.Users;
import com.hotelmanagementsystem.authservice.repository.RefreshTokenRepository;
import com.hotelmanagementsystem.authservice.repository.UserRepository;
import com.hotelmanagementsystem.authservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${spring.security.jwt.refresh-token.expiration-time:604800000}") // 7 days
    private long refreshTokenDurationMs;

    @Value("${spring.security.jwt.expiration-time}")
    private long accessTokenDurationMs;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create new user
        Users user = new Users();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : UserRole.USER);
        user.setIsActive(true);

        // Save user
        Users savedUser = userRepository.save(user);

        // Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String accessToken = jwtTokenProvider.generateToken(userDetails, savedUser.getRole().name());
        String refreshToken = createRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenDurationMs / 1000) // Convert to seconds
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Load user details
            Users user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            if (!user.getIsActive()) {
                throw new BadCredentialsException("User account is disabled");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

            // Revoke existing refresh tokens
            refreshTokenRepository.revokeAllUserTokens(user);

            // Generate new tokens
            String accessToken = jwtTokenProvider.generateToken(userDetails, user.getRole().name());
            String refreshToken = createRefreshToken(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(accessTokenDurationMs / 1000)
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Transactional
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        // Validate refresh token format
        if (!jwtTokenProvider.validateRefreshToken(requestRefreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token format");
        }

        // Extract username from refresh token
        String username = jwtTokenProvider.extractUsername(requestRefreshToken);

        // Find refresh token in database
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        // Check if token is expired or revoked
        if (refreshToken.isExpired() || refreshToken.getRevoked()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token was expired or revoked");
        }

        // Load user
        Users user = refreshToken.getUser();
        if (!user.getIsActive()) {
            throw new BadCredentialsException("User account is disabled");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // Generate new access token
        String accessToken = jwtTokenProvider.generateToken(userDetails, user.getRole().name());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(requestRefreshToken) // Return the same refresh token
                .tokenType("Bearer")
                .expiresIn(accessTokenDurationMs / 1000)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        refreshTokenRepository.delete(token);
    }

    private String createRefreshToken(Users user) {
        // Generate JWT refresh token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String tokenValue = jwtTokenProvider.generateRefreshToken(userDetails);

        // Save to database
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(tokenValue);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000));

        refreshTokenRepository.save(refreshToken);
        return tokenValue;
    }

    public UserValidationResponse validateToken(String authHeader) {
        try {
            // Remove "Bearer " prefix
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return UserValidationResponse.builder()
                        .valid(false)
                        .message("Invalid authorization header format")
                        .build();
            }

            String token = authHeader.substring(7);

            // Check if token is expired first (will throw exception if malformed)
            if (jwtTokenProvider.isTokenExpired(token)) {
                return UserValidationResponse.builder()
                        .valid(false)
                        .message("Token is expired")
                        .build();
            }

            // Extract username from token
            String username = jwtTokenProvider.extractUsername(token);

            if (username == null || username.isEmpty()) {
                return UserValidationResponse.builder()
                        .valid(false)
                        .message("Invalid token: unable to extract username")
                        .build();
            }

            // Load user from database
            Users user = userRepository.findByUsername(username)
                    .orElse(null);

            if (user == null) {
                return UserValidationResponse.builder()
                        .valid(false)
                        .message("User not found")
                        .build();
            }

            // Check if user is active
            if (!user.getIsActive()) {
                return UserValidationResponse.builder()
                        .valid(false)
                        .message("User account is disabled")
                        .build();
            }

            // Return successful validation
            return UserValidationResponse.builder()
                    .valid(true)
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .message("Token is valid")
                    .build();

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return UserValidationResponse.builder()
                    .valid(false)
                    .message("Token has expired")
                    .build();
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            return UserValidationResponse.builder()
                    .valid(false)
                    .message("Malformed JWT token")
                    .build();
        } catch (io.jsonwebtoken.SignatureException e) {
            return UserValidationResponse.builder()
                    .valid(false)
                    .message("Invalid JWT signature")
                    .build();
        } catch (Exception e) {
            return UserValidationResponse.builder()
                    .valid(false)
                    .message("Token validation failed: " + e.getMessage())
                    .build();
        }
    }
}