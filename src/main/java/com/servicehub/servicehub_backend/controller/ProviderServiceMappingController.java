package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.ProviderServiceMappingDTO;
import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.service.ProviderServiceMappingService;
import com.servicehub.servicehub_backend.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provider-services")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProviderServiceMappingController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderServiceMappingController.class);

    private final ProviderServiceMappingService providerServiceMappingService;

    public ProviderServiceMappingController(ProviderServiceMappingService providerServiceMappingService) {
        this.providerServiceMappingService = providerServiceMappingService;
    }

    @GetMapping("")
    public ResponseEntity<StandardResponse<List<ProviderServiceMappingDTO>>> getProviderServices() {
        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        logger.info("Fetching services for provider with email: {}", providerEmail);

        try {
            List<ProviderServiceMappingDTO> services = providerServiceMappingService.getServicesByProviderId(providerEmail);
            logger.info("Successfully retrieved {} services for provider: {}", services.size(), providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Services fetched successfully", services));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new StandardResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error while fetching services for provider {}: {}", providerEmail, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new StandardResponse<>(false, "An error occurred", null));
        }
    }


    @PutMapping("")
    public ResponseEntity<StandardResponse<List<ProviderServiceMappingDTO>>> addServices(
            @RequestBody List<ProviderServiceMappingDTO> providerServiceMappingDTOs) {

        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        logger.info("Updating {} services for provider with email: {}", providerServiceMappingDTOs.size(), providerEmail);

        try {
            List<ProviderServiceMappingDTO> updatedServices = providerServiceMappingService.addServices(providerEmail, providerServiceMappingDTOs);
            logger.info("Successfully updated {} services for provider: {}", updatedServices.size(), providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Services updated successfully", updatedServices));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new StandardResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error while updating services for provider {}: {}", providerEmail, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new StandardResponse<>(false, "An error occurred", null));
        }
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<StandardResponse<String>> deleteService(
            @PathVariable Long serviceId) {

        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        logger.info("Deleting service with ID {} for provider: {}", serviceId, providerEmail);

        try {
            providerServiceMappingService.deleteService(providerEmail, serviceId);
            logger.info("Successfully deleted service ID {} for provider: {}", serviceId, providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Service deleted successfully", null));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new StandardResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error deleting service ID {} for provider {}: {}", serviceId, providerEmail, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new StandardResponse<>(false, "An error occurred", null));
        }
    }

}
