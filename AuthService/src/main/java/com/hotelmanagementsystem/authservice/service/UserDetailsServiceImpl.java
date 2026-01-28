package com.hotelmanagementsystem.authservice.service;

import com.hotelmanagementsystem.authservice.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        // Map role(s) to Spring Security authorities with ROLE_ prefix
        List<SimpleGrantedAuthority> authorities = switchAuthorityMapping(user);

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private List<SimpleGrantedAuthority> switchAuthorityMapping(com.hotelmanagementsystem.authservice.entity.Users user) {
        // If your Users has a single enum field getRole()
        var roleName = "ROLE_" + user.getRole().name(); // USER, MODERATOR, ADMIN -> ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN
        return List.of(new SimpleGrantedAuthority(roleName));

    }
}
