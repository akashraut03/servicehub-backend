package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryInfoEntity,Long> {
}
