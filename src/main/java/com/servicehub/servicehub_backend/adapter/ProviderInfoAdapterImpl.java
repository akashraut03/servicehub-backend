package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.ProviderInfoDTO;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProviderInfoAdapterImpl implements ProviderInfoAdapter {

    public ProviderInfoDTO toDTO(ProviderInfoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProviderInfoDTO(
                entity.getFullName(),
                entity.getAddress(),
                entity.getGender(),
                entity.getDob(),
                "12233456665",
                entity.getPhoneNumber()
        );
    }

    public ProviderInfoEntity toEntity(ProviderInfoDTO dto) {
        if (dto == null) {
            return null;
        }

        ProviderInfoEntity entity = new ProviderInfoEntity();
        entity.setFullName(dto.getFullName());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setDob(dto.getDob());
        entity.setAadhaarCardNumber(dto.getAadhaarCardNumber());
        entity.setPhoneNumber(dto.getPhoneNumber());

        return entity;
    }
}
