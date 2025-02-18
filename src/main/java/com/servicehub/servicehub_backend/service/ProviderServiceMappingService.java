package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.dto.ProviderServiceMappingDTO;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import com.servicehub.servicehub_backend.model.ProviderServiceMappingEntity;
import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import com.servicehub.servicehub_backend.repository.ProviderRepository;
import com.servicehub.servicehub_backend.repository.ProviderServiceMappingRepository;
import com.servicehub.servicehub_backend.repository.ServiceCategoryInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProviderServiceMappingService {

    private final ProviderServiceMappingRepository providerServiceMappingRepository;
    private final ProviderRepository providerRepository;
    private final ServiceCategoryInfoRepository serviceCategoryInfoRepository;

    public ProviderServiceMappingService(ProviderServiceMappingRepository providerServiceMappingRepository,
                                         ProviderRepository providerRepository,
                                         ServiceCategoryInfoRepository serviceCategoryInfoRepository) {
        this.providerServiceMappingRepository = providerServiceMappingRepository;
        this.providerRepository = providerRepository;
        this.serviceCategoryInfoRepository = serviceCategoryInfoRepository;
    }

    public List<ProviderServiceMappingDTO> addServices(String providerEmail, List<ProviderServiceMappingDTO> providerServiceMappingDTOs) {
        if (providerServiceMappingDTOs == null || providerServiceMappingDTOs.isEmpty()) {
            log.warn("Invalid request: No services provided");
            throw new IllegalArgumentException("No services provided");
        }

        Optional<ProviderInfoEntity> providerOptional = providerRepository.findByUser_Email(providerEmail);
        if (providerOptional.isEmpty()) {
            log.error("Provider not found for email: {}", providerEmail);
            throw new IllegalArgumentException("Provider not found");
        }

        ProviderInfoEntity provider = providerOptional.get();
        List<ProviderServiceMappingDTO> addedServiceDTOs = new ArrayList<>();

        for (ProviderServiceMappingDTO dto : providerServiceMappingDTOs) {
            if (dto.getServiceId() == null || dto.getPrice() == null) {
                log.warn("Skipping service due to missing fields: {}", dto);
                continue;
            }

            Optional<ServiceCategoryInfoEntity> serviceOptional = serviceCategoryInfoRepository.findById(dto.getServiceId());
            if (serviceOptional.isEmpty()) {
                log.warn("Skipping service: Service not found for ID {}", dto.getServiceId());
                continue;
            }

            ProviderServiceMappingEntity providerServiceMapping = new ProviderServiceMappingEntity();
            providerServiceMapping.setProvider(provider);
            providerServiceMapping.setService(serviceOptional.get());
            providerServiceMapping.setPrice(dto.getPrice());

            ProviderServiceMappingEntity savedServiceMapping = providerServiceMappingRepository.save(providerServiceMapping);
            addedServiceDTOs.add(convertToDTO(savedServiceMapping));
        }

        log.info("{} services added successfully for provider: {}", addedServiceDTOs.size(), providerEmail);
        return addedServiceDTOs;
    }

    public List<ProviderServiceMappingDTO> getServicesByProviderId(String providerEmail) {
        Optional<ProviderInfoEntity> providerOptional = providerRepository.findByUser_Email(providerEmail);
        if (providerOptional.isEmpty()) {
            log.error("Provider not found for email: {}", providerEmail);
            throw new IllegalArgumentException("Provider not found");
        }
        List<ProviderServiceMappingEntity> services = providerServiceMappingRepository.findByProvider_Id(providerOptional.get().getId());

        if (services.isEmpty()) {
            log.info("No services found for provider ID: {}", providerOptional.get().getId());
            return Collections.emptyList();
        }

        return services.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteService(String providerEmail, Long serviceId) {
        Optional<ProviderInfoEntity> providerOptional = providerRepository.findByUser_Email(providerEmail);
        if (providerOptional.isEmpty()) {
            log.error("Provider not found for email: {}", providerEmail);
            throw new IllegalArgumentException("Provider not found");
        }

        ProviderInfoEntity provider = providerOptional.get();

        Optional<ProviderServiceMappingEntity> mappingOptional =
                Optional.ofNullable(providerServiceMappingRepository.findByProvider_IdAndServiceId(provider.getId(), serviceId));

        if (mappingOptional.isEmpty()) {
            log.warn("Service ID {} not found for provider {}", serviceId, providerEmail);
            throw new IllegalArgumentException("Service not found for provider");
        }

        providerServiceMappingRepository.delete(mappingOptional.get());
        log.info("Service ID {} deleted successfully for provider: {}", serviceId, providerEmail);
    }



    private ProviderServiceMappingDTO convertToDTO(ProviderServiceMappingEntity entity) {
        ProviderServiceMappingDTO dto = new ProviderServiceMappingDTO();
        dto.setId(entity.getId());
        dto.setServiceName(entity.getService().getName());
        dto.setServiceId(entity.getService().getId());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
