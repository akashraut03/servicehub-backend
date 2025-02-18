package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.adapter.ProviderInfoAdapter;
import com.servicehub.servicehub_backend.dto.ProviderInfoDTO;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import com.servicehub.servicehub_backend.model.UserEntity;
import com.servicehub.servicehub_backend.repository.ProviderRepository;
import com.servicehub.servicehub_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProvideServiceImpl implements ProviderService {

    private static final Logger logger = LoggerFactory.getLogger(ProvideServiceImpl.class);
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final ProviderInfoAdapter providerInfoAdapter;


    @Autowired
    public ProvideServiceImpl(final ProviderRepository providerRepository, final UserRepository userRepository, final ProviderInfoAdapter providerInfoAdapter) {
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
        this.providerInfoAdapter = providerInfoAdapter;
    }


    @Override
    public ProviderInfoDTO getProvider(String providerEmail) {
        UserEntity user = userRepository.findByEmail(providerEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + providerEmail));

        Optional<ProviderInfoEntity> existingProvider = providerRepository.findByUser_Email(providerEmail);
        if (existingProvider.isPresent()) {
            logger.info("Found provider: {}", existingProvider.get());
            return providerInfoAdapter.toDTO(existingProvider.get());
        } else {
            logger.warn("Provider not found for email: {}", providerEmail);
            return null;
        }
    }

    @Override
    @Transactional
    public ProviderInfoDTO addProvider(final String providerEmail, final ProviderInfoDTO providerInfoDTO) {
        logger.info("Adding/updating provider with email: {}", providerEmail);

        UserEntity userEntity = userRepository.findByEmail(providerEmail).orElseThrow(() -> {
            logger.error("User not found for email: {}", providerEmail);
            return new RuntimeException("User not found");
        });

        Optional<ProviderInfoEntity> existingProvider = providerRepository.findByUser_Email(providerEmail);

        if (existingProvider.isPresent()) {
            ProviderInfoEntity existingProviderEntity = existingProvider.get();

            existingProviderEntity.setPhoneNumber(providerInfoDTO.getPhoneNumber());
            existingProviderEntity.setAddress(providerInfoDTO.getAddress());
            existingProviderEntity.setGender(providerInfoDTO.getGender());
            existingProviderEntity.setDob(providerInfoDTO.getDob());
            existingProviderEntity.setFullName(providerInfoDTO.getFullName());
            existingProviderEntity.setAadhaarCardNumber(providerInfoDTO.getAadhaarCardNumber());

            logger.debug("Updating provider details: {}", existingProviderEntity);
            existingProviderEntity = providerRepository.save(existingProviderEntity);

            ProviderInfoDTO dto = providerInfoAdapter.toDTO(existingProviderEntity);
            logger.debug("Converted DTO: {}", dto);
            return dto;
        } else {
            ProviderInfoEntity newProviderEntity = providerInfoAdapter.toEntity(providerInfoDTO);
            newProviderEntity.setUser(userEntity);

            logger.debug("Creating new provider with details: {}", newProviderEntity);
            newProviderEntity = providerRepository.save(newProviderEntity);

            ProviderInfoDTO dto = providerInfoAdapter.toDTO(newProviderEntity);
            logger.debug("Converted DTO: {}", dto);
            return dto;
        }
    }


//
//    @Override
//    public List<ProviderServicesModel> getProvidersByService(String serviceName) {
//        List<ProviderServicesModel> providers = providerServiceRepository.findByService_ServiceName(serviceName);
//        if (providers.isEmpty()) {
//            throw new RuntimeException("No providers available for this service: " + serviceName);
//        }
//        return providers;
//    }
//
//    @Override
//    public List<ServiceModel> getAllService() {
//        return serviceRepository.findAll().stream().toList();
//    }
//
//    @Override
//    public String addServicesForProvider(final String providerEmail, List<ProviderServicesModel> providerServices) {
//        ProviderModel provider = providerRepository.findByUser_Email(providerEmail)
//                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + providerEmail));
//        if (!isProfileComplete(providerEmail)) {
//            throw new RuntimeException("Provider must complete their profile before adding services.");
//        }
//        for (ProviderServicesModel serviceModel : providerServices) {
//            if (serviceModel == null) {
//                return "Service with name " + serviceModel + " not found.";
//            }
//
//            ProviderServicesModel providerService = new ProviderServicesModel();
//            providerService.setProvider(provider);
//
//            providerServiceRepository.save(providerService);
//        }
//        return "Services added successfully to the provider.";
//    }
//
//    public boolean isProfileComplete(String email) {
//        ProviderModel provider = providerRepository.findByUser_Email(email)
//                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));
//        return provider.getName() != null && !provider.getName().isEmpty() &&
//                provider.getPhoneNumber() != null && !provider.getPhoneNumber().isEmpty() &&
//                provider.getAddress() != null && !provider.getAddress().isEmpty();
//    }
}
