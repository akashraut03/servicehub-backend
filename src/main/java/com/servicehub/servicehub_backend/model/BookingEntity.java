package com.servicehub.servicehub_backend.model;

import com.servicehub.servicehub_backend.constant.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking_form")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resident_id", referencedColumnName = "id", nullable = false)
    private ResidentInfoEntity resident;

    @ManyToOne
    @JoinColumn(name = "service_provider_id", referencedColumnName = "id")
    private ProviderInfoEntity serviceProvider;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = false)
    private ServiceCategoryInfoEntity service;

    private LocalDate bookingDate;
    private String timeSlot;
    private String address;
    private String additionalInfo;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
