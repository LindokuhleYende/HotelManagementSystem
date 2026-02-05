package com.hotelmanagementsystem.roombookingservice.authConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements Principal {
    private String username;
    private String role;

    @Override
    public String getName() {
        return username;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isModerator() {
        return "MODERATOR".equals(role);
    }

    public boolean isUser() {
        return "USER".equals(role);
    }
}