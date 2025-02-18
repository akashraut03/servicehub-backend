package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.constant.Role;
import com.servicehub.servicehub_backend.model.UserEntity;
import com.servicehub.servicehub_backend.repository.UserRepository;
import com.servicehub.servicehub_backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserEntity createUser(UserEntity user, HttpServletResponse response) {
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Signup failed: Email already exists - {}", user.getEmail());
            return null;
        }

        String role = user.getRole() != null ? user.getRole().toString() : Role.RESIDENT.toString();
        String token = jwtUtil.generateToken(user.getEmail(), role);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.RESIDENT);
        user.setJwtToken(token);

        userRepository.save(user);
        user.setPassword(null);

        // Set JWT cookie after user creation
        setJwtInCookie(response, token);

        logger.info("User created successfully: {}", user.getEmail());
        return user;
    }

    @Override
    public UserEntity loginUser(UserEntity user, HttpServletRequest request, HttpServletResponse response) {
        String token = extractJwtFromCookie(request);

        if (token != null) {
            String email = jwtUtil.extractEmail(token);
            Optional<UserEntity> existingUser = userRepository.findByEmail(email);

            // If the token is valid, return the existing user
            if (existingUser.isPresent() && jwtUtil.validateToken(token, createUserDetails(existingUser.get()))) {
                logger.info("JWT validated successfully for email: {}", email);
                // Token is valid, set it again in the cookie (e.g., to refresh expiry)
                setJwtInCookie(response, token);
                return existingUser.get();
            } else {
                // Token is invalid or expired, clear the token cookie
                clearJwtCookie(response);
                logger.warn("JWT is invalid or expired for email: {}", email);
            }
        }

        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            boolean isPasswordMatch = passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword());
            if (isPasswordMatch) {
                existingUser.get().setPassword(null);  // Clear password from the response
                logger.info("Password validated successfully for email: {}", user.getEmail());

                token = jwtUtil.generateToken(user.getEmail(), existingUser.get().getRole().toString());
                setJwtInCookie(response, token);

                return existingUser.get();
            } else {
                logger.warn("Login failed: Invalid password for email: {}", user.getEmail());
            }
        }

        logger.warn("Login failed: Invalid credentials for email: {}", user.getEmail());
        return null;
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            logger.warn("JWT token not found: No cookies present in the request.");
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                logger.debug("JWT token extracted from cookie.");
                return cookie.getValue();
            }
        }

        logger.warn("JWT token not found in cookies.");
        return null;
    }

    private void setJwtInCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("token", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);  // Make sure this is set to true for secure connections (HTTPS)
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24 * 30);  // Set cookie expiration to 30 days
        response.addCookie(jwtCookie);
        logger.debug("JWT token added to cookie.");
    }

    private UserDetails createUserDetails(UserEntity userEntity) {
        return org.springframework.security.core.userdetails.User.builder().username(userEntity.getEmail()) // Email as the username
                .password(userEntity.getPassword()) // Password
                .authorities(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().toString())) // Role
                .build();
    }


    private void clearJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);  // Make sure this is set to true for secure connections (HTTPS)
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);  // Expire the cookie immediately
        response.addCookie(jwtCookie);
        logger.debug("JWT token cookie cleared.");
    }
}
