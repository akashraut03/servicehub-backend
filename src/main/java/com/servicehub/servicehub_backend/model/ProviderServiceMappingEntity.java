package com.servicehub.servicehub_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "provider_services")
public class ProviderServiceMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderInfoEntity provider;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceCategoryInfoEntity service;

    @Column(nullable = false)
    private BigDecimal price;

    public ProviderServiceMappingEntity() {
    }

    public ProviderServiceMappingEntity(ProviderInfoEntity provider, ServiceCategoryInfoEntity service, BigDecimal price) {
        this.provider = provider;
        this.service = service;
        this.price = price;
    }
}
