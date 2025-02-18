package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.adapter.ResidentInfoAdapter;
import com.servicehub.servicehub_backend.dto.ResidentInfoDTO;
import com.servicehub.servicehub_backend.model.ResidentInfoEntity;
import com.servicehub.servicehub_backend.model.UserEntity;
import com.servicehub.servicehub_backend.repository.ResidentRepository;
import com.servicehub.servicehub_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ResidentServiceImpl implements ResidentService {

    private static final Logger logger = LoggerFactory.getLogger(ResidentServiceImpl.class);
    private final ResidentRepository residentRepository;
    private final UserRepository userRepository;
    private final ResidentInfoAdapter residentInfoAdapter;

    @Autowired
    public ResidentServiceImpl(ResidentRepository residentRepository, UserRepository userRepository, ResidentInfoAdapter residentInfoAdapter) {
        this.residentRepository = residentRepository;
        this.userRepository = userRepository;
        this.residentInfoAdapter = residentInfoAdapter;
    }


    @Override
    public ResidentInfoDTO getResidentDetail(final String email) {
        logger.info("Looking for resident with email: {}", email);

        Optional<ResidentInfoEntity> resident = residentRepository.findByUser_Email(email);

        if (resident.isPresent()) {
            logger.info("Found resident: {}", resident.get());
            return residentInfoAdapter.toDTO(resident.get());
        } else {
            logger.warn("Resident not found for email: {}", email);
            return null;
        }
    }

    @Override
    @Transactional
    public ResidentInfoDTO saveResidentProfile(String residentEmail, ResidentInfoDTO resident) {
        logger.info("Saving profile for resident with email: {}", residentEmail);

        UserEntity userEntity = userRepository.findByEmail(residentEmail).orElseThrow(() -> {
            logger.error("User not found for email: {}", residentEmail);
            return new RuntimeException("User not found");
        });

        Optional<ResidentInfoEntity> existingResident = residentRepository.findByUser_Email(residentEmail);

        if (existingResident.isPresent()) {
            ResidentInfoEntity residentEntity = existingResident.get();

            residentEntity.setAddress(resident.getAddress());
            residentEntity.setFullName(resident.getFullName());
//            residentEntity.setPhoneNumber(resident.getPhoneNumber());
            residentEntity.setGender(resident.getGender());
            residentEntity.setDob(resident.getDob());

            logger.debug("Updating resident profile: {}", residentEntity);
            residentRepository.save(residentEntity);

            logger.info("Resident profile updated successfully for email: {}", residentEmail);
            return residentInfoAdapter.toDTO(residentEntity);
        } else {
            ResidentInfoEntity residentEntity = residentInfoAdapter.toEntity(resident);
            residentEntity.setUser(userEntity);

            logger.debug("Creating new resident profile: {}", residentEntity);
            residentRepository.save(residentEntity);

            logger.info("New resident profile created successfully for email: {}", residentEmail);
            return residentInfoAdapter.toDTO(residentEntity);
        }
    }
}
