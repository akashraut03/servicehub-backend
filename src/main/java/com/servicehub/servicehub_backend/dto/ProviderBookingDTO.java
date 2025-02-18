package com.servicehub.servicehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderBookingDTO {
    private Long bookingId;
    private String serviceName;
    private String residentName;
    private LocalDate bookingDate;
    private String timeSlot;
    private String address;
    private String status;
}


