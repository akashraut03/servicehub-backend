package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.BookingDTO;
import com.servicehub.servicehub_backend.model.BookingEntity;

public interface BookingFormAdapter {

    BookingEntity toEntity(BookingDTO dto);

    BookingDTO toDTO(BookingEntity entity);
}
