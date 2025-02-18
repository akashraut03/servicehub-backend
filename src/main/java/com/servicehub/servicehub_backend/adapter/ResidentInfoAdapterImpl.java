package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.ResidentInfoDTO;
import com.servicehub.servicehub_backend.model.ResidentInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class ResidentInfoAdapterImpl implements ResidentInfoAdapter {

    @Override
    public ResidentInfoEntity toEntity(ResidentInfoDTO request) {
        ResidentInfoEntity entity = new ResidentInfoEntity();
        entity.setFullName(request.getFullName());
        entity.setAddress(request.getAddress());
        entity.setGender(request.getGender());
        entity.setDob(request.getDob());
        return entity;
    }

    @Override
    public ResidentInfoDTO toDTO(ResidentInfoEntity entity) {
        return new ResidentInfoDTO(
                entity.getId(),
                entity.getFullName(),
                entity.getAddress(),
                entity.getGender(),
                entity.getDob()
        );
    }
}

