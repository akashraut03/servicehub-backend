package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;

import java.util.List;

public interface ServiceCategoryService {
    List<ServiceCategoryInfoEntity> getAllCategories();
}
