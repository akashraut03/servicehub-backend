package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.dto.ResidentInfoDTO;
import com.servicehub.servicehub_backend.model.ResidentInfoEntity;

public interface ResidentService {
    ResidentInfoDTO getResidentDetail(String email);
    ResidentInfoDTO saveResidentProfile(String email, ResidentInfoDTO residentModel);
}
