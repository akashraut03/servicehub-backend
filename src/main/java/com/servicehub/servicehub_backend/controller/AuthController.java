package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.model.UserEntity;
import com.servicehub.servicehub_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    final private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<StandardResponse<UserEntity>> register(@RequestBody UserEntity user, HttpServletResponse response) {
        try {
            UserEntity signUpResponse = authService.createUser(user, response);
            if (signUpResponse != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new StandardResponse<>(true, "Signup successful", signUpResponse));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new StandardResponse<>(false, "Signup failed: User could not be created", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(false, e.getMessage(), user));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<UserEntity>> login(@RequestBody UserEntity user, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserEntity loginResponse = authService.loginUser(user, request, response);
            if (loginResponse != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new StandardResponse<>(true, "User login successful", loginResponse));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new StandardResponse<>(false, "Invalid username or password", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(false, e.getMessage(), user));
        }

    }
}
