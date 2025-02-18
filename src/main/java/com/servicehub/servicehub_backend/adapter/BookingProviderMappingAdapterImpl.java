package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.BookingProviderMappingDTO;
import com.servicehub.servicehub_backend.model.BookingEntity;
import com.servicehub.servicehub_backend.model.BookingProviderMappingEntity;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingProviderMappingAdapterImpl implements BookingProviderMappingAdapter {

    @Override
    public BookingProviderMappingDTO toDTO(BookingProviderMappingEntity entity) {
        if (entity == null) {
            return null;
        }

        BookingProviderMappingDTO dto = new BookingProviderMappingDTO();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBooking() != null ? entity.getBooking().getId() : null);
        dto.setProviderId(entity.getProvider() != null ? entity.getProvider().getId() : null);
        dto.setStatus(entity.getStatus());
        return dto;
    }

    @Override
    public BookingProviderMappingEntity toEntity(BookingProviderMappingDTO dto) {
        if (dto == null) {
            return null;
        }

        BookingProviderMappingEntity entity = new BookingProviderMappingEntity();
        entity.setId(dto.getId());

        BookingEntity booking = new BookingEntity();
        booking.setId(dto.getBookingId());
        entity.setBooking(booking);

        ProviderInfoEntity provider = new ProviderInfoEntity();
        provider.setId(dto.getProviderId());
        entity.setProvider(provider);

        entity.setStatus(dto.getStatus());
        return entity;
    }
}
