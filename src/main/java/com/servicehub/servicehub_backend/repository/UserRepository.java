package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}
