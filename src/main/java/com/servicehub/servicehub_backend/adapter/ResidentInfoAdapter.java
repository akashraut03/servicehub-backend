package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.ResidentInfoDTO;
import com.servicehub.servicehub_backend.model.ResidentInfoEntity;

public interface ResidentInfoAdapter {
    ResidentInfoEntity toEntity(ResidentInfoDTO dto);
    ResidentInfoDTO toDTO(ResidentInfoEntity entity);
}
