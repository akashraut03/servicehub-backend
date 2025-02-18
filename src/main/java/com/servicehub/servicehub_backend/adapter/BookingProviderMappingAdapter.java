package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.BookingProviderMappingDTO;
import com.servicehub.servicehub_backend.model.BookingProviderMappingEntity;

public interface BookingProviderMappingAdapter {
    BookingProviderMappingDTO toDTO(BookingProviderMappingEntity entity);

    BookingProviderMappingEntity toEntity(BookingProviderMappingDTO dto);
}
