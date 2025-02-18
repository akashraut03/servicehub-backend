package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.BookingProviderMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingProviderMappingRepository extends JpaRepository<BookingProviderMappingEntity, Long> {
    Optional<BookingProviderMappingEntity> findByBooking_IdAndProvider_Id(Long bookingId, Long providerId);

}
