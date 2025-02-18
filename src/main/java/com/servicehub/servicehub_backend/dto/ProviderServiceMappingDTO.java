package com.servicehub.servicehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderServiceMappingDTO {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private BigDecimal price;
}
