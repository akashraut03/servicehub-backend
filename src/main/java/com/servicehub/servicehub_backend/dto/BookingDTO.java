package com.servicehub.servicehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class BookingDTO {

    private Long id;
    private Long residentId;
    private Long serviceProviderId;
    private Long serviceId;
    private String serviceName;
    private LocalDate bookingDate;
    private String timeSlot;
    private String address;
    private String additionalInfo;
    private String status;

    public BookingDTO() {

    }
}
