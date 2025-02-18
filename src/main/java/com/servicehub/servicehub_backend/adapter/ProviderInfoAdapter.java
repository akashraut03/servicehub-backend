package com.servicehub.servicehub_backend.adapter;

import com.servicehub.servicehub_backend.dto.ProviderInfoDTO;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
public interface ProviderInfoAdapter {

    ProviderInfoDTO toDTO(ProviderInfoEntity entity);

    ProviderInfoEntity toEntity(ProviderInfoDTO dto);
}
