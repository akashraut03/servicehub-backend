package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.ResidentInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<ResidentInfoEntity, Long> {
    Optional<ResidentInfoEntity> findByUser_Email(String email);
}
