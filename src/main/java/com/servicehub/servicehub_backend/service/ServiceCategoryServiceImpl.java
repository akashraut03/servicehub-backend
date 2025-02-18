package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import com.servicehub.servicehub_backend.repository.ServiceCategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    final private ServiceCategoryInfoRepository serviceCategoryInfoRepository;

    @Autowired
    public ServiceCategoryServiceImpl(ServiceCategoryInfoRepository serviceCategoryInfoRepository) {
        this.serviceCategoryInfoRepository = serviceCategoryInfoRepository;
    }

    @Override
    public List<ServiceCategoryInfoEntity> getAllCategories() {
        List<ServiceCategoryInfoEntity> allCategories = serviceCategoryInfoRepository.findAll();
        return allCategories;
    }
}
