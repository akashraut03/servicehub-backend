package com.servicehub.servicehub_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;


import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "service_category_info")
public class ServiceCategoryInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProviderServiceMappingEntity> providerServices;

    public ServiceCategoryInfoEntity() {
    }
}
