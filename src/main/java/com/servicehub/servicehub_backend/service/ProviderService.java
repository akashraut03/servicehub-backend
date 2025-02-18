package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.dto.ProviderInfoDTO;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;

public interface ProviderService {
    ProviderInfoDTO addProvider(String providerEmail, ProviderInfoDTO provider);

    ProviderInfoDTO getProvider(String providerEmail);

//    List<ProviderServicesModel> getProvidersByService(String serviceName);
//
//    List<ServiceModel> getAllService();
//
//    boolean isProfileComplete(String email);
//
//    String addServicesForProvider(String email, List<ProviderServicesModel> providerServices);
}
