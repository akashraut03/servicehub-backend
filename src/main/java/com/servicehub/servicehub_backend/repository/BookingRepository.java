package com.servicehub.servicehub_backend.repository;

import com.servicehub.servicehub_backend.model.BookingEntity;
import com.servicehub.servicehub_backend.model.BookingProviderMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    @Query("SELECT b FROM BookingEntity b " +
            "WHERE b.resident.id = :residentId " +
            "AND (:status IS NULL OR b.status = :status)")
    List<BookingEntity> findBookingsWithStatusByResident(Long residentId, String status);

    @Query("SELECT bpm FROM BookingProviderMappingEntity bpm " +
            "JOIN bpm.booking b " +
            "WHERE b.serviceProvider.id = :providerId " +
            "AND (:status IS NULL OR bpm.status = :status)")
    List<BookingProviderMappingEntity> findBookingsWithStatusByProvider(Long providerId, String status);
}
