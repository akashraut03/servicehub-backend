package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.ProviderInfoDTO;
import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.service.ProviderService;
import com.servicehub.servicehub_backend.util.AuthUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provider")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProviderController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StandardResponse<ProviderInfoDTO>> getProvider() {
        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        logger.info("Fetching provider details for email: {}", providerEmail);

        try {
            ProviderInfoDTO provider = providerService.getProvider(providerEmail);

            if (provider == null) {
                logger.warn("No provider profile found for email: {}", providerEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new StandardResponse<>(false, "Provider profile not found", null));
            }

            logger.info("Provider details retrieved successfully for email: {}", providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Provider details retrieved", provider));
        } catch (Exception e) {
            logger.error("Error fetching provider for email: {} | Error: {}", providerEmail, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error fetching provider details", null));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<StandardResponse<ProviderInfoDTO>> register(@Valid @RequestBody ProviderInfoDTO provider) {
        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        logger.info("Registering provider with email: {}", providerEmail);

        try {
            if (provider == null) {
                logger.error("Provider data is null in the request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new StandardResponse<>(false, "Invalid provider data", null));
            }

            providerService.addProvider(providerEmail, provider);
            logger.info("Provider registered successfully with email: {}", providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Provider added successfully", provider));
        } catch (Exception e) {
            logger.error("Error registering provider: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error registering provider", null));
        }
    }
}
