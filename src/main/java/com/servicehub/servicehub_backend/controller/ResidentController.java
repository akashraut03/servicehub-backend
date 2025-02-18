package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.ResidentInfoDTO;
import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.service.ResidentService;
import com.servicehub.servicehub_backend.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resident")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ResidentController {
    private static final Logger logger = LoggerFactory.getLogger(ResidentController.class);
    private final ResidentService residentService;

    @Autowired
    public ResidentController(final ResidentService residentService) {
        this.residentService = residentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StandardResponse<ResidentInfoDTO>> getResident() {
        try {
            String email = AuthUtil.getAuthenticatedUserEmail();

            logger.info("Fetching resident details for email: {}", email);

            ResidentInfoDTO resident = residentService.getResidentDetail(email);
            if (resident != null) {
                return ResponseEntity.ok(new StandardResponse<>(true, "Resident details fetched successfully", resident));
            } else {
                logger.error("Resident not found for email: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardResponse<>(false, "Resident not found", null));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching resident details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error fetching resident details", null));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<StandardResponse<ResidentInfoDTO>> saveResident(@RequestBody ResidentInfoDTO residentModel) {
        try {
            String email = AuthUtil.getAuthenticatedUserEmail();
            logger.info("Saving resident profile for email: {}", email);

            ResidentInfoDTO savedResident = residentService.saveResidentProfile(email, residentModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new StandardResponse<>(true, "Resident profile saved successfully", savedResident));
        } catch (Exception e) {
            logger.error("Error occurred while saving resident profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error saving resident profile", null));
        }
    }
}
