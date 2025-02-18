package com.servicehub.servicehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingProviderMappingDTO {

    private Long id;
    private Long bookingId;
    private Long providerId;
    private String status;
}
