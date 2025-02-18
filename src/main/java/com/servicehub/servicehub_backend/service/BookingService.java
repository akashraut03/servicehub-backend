package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.dto.BookingDTO;
import com.servicehub.servicehub_backend.dto.BookingProviderMappingDTO;
import com.servicehub.servicehub_backend.dto.ProviderBookingDTO;
import com.servicehub.servicehub_backend.dto.ResidentBookingDTO;

import java.util.List;

public interface BookingService {


    BookingDTO createBooking(String residentEmail, BookingDTO bookingDTO);

    BookingProviderMappingDTO acceptBookingByProvider(Long bookingId, String providerEmail);

    BookingDTO confirmProviderByResident(Long bookingId, Long providerId);

    List<BookingDTO> getResidentBookingCountsByStatus(String residentEmail, String status);
    List<ProviderBookingDTO> getProviderBookingCountsByStatus(String residentEmail, String status);
    List<BookingDTO> getBookingsByStatus(String residentEmail, String status);

}
