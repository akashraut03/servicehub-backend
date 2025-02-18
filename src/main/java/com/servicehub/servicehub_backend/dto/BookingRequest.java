package com.servicehub.servicehub_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingRequest {
    private String name;
    private String contact;
    private String address;
    private Date preferredDate;
    private String notes;
    private String serviceName;
    private Long residentId;
}
