package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.model.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserEntity createUser(UserEntity user, HttpServletResponse response);

    UserEntity loginUser(UserEntity user, HttpServletRequest request, HttpServletResponse response);
}
