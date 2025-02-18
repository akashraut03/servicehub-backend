package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCategoryInfoRepository extends JpaRepository<ServiceCategoryInfoEntity, Long> {
}
