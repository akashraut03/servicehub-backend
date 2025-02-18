package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.ProviderServiceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderServiceMappingRepository extends JpaRepository<ProviderServiceMappingEntity, Long> {
    List<ProviderServiceMappingEntity> findByProvider_Id(Long providerId);

    ProviderServiceMappingEntity findByProvider_IdAndServiceId(Long providerId, Long serviceId);

}
