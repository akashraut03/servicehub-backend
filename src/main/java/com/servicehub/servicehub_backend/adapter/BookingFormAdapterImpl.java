package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.BookingDTO;
import com.servicehub.servicehub_backend.constant.BookingStatus;
import com.servicehub.servicehub_backend.model.BookingEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingFormAdapterImpl implements BookingFormAdapter {

    // Convert BookingFormDTO to BookingFormEntity
    public BookingEntity toEntity(BookingDTO dto) {
        if (dto == null) {
            return null;
        }

        BookingEntity entity = new BookingEntity();
        entity.setId(dto.getId());
        entity.getResident().setId(dto.getResidentId());
        entity.getServiceProvider().setId(dto.getServiceProviderId());
        entity.getService().setId(dto.getServiceId());
        entity.setTimeSlot(dto.getTimeSlot());
        entity.setBookingDate(dto.getBookingDate());
        entity.setAddress(dto.getAddress());
        entity.setStatus(BookingStatus.valueOf(dto.getStatus()));
        return entity;
    }

    // Convert BookingFormEntity to BookingFormDTO
    public BookingDTO toDTO(BookingEntity entity) {
        if (entity == null) {
            return null;
        }

        BookingDTO dto = new BookingDTO();
        dto.setId(entity.getId());
        dto.setResidentId(entity.getResident().getId());
        dto.setServiceId(entity.getService().getId());
        dto.setTimeSlot(entity.getTimeSlot());
        dto.setBookingDate(entity.getBookingDate());
        dto.setAddress(entity.getAddress());
        dto.setStatus(String.valueOf(entity.getStatus()));

        return dto;
    }
}
