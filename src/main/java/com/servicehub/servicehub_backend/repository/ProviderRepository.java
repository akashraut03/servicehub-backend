package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderInfoEntity, Long> {
    Optional<ProviderInfoEntity> findByUser_Email(String email);

}
