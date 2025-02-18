package com.servicehub.servicehub_backend.controller;

import com.servicehub.servicehub_backend.dto.BookingDTO;
import com.servicehub.servicehub_backend.dto.BookingProviderMappingDTO;
import com.servicehub.servicehub_backend.dto.NotificationDTO;
import com.servicehub.servicehub_backend.dto.ProviderBookingDTO;
import com.servicehub.servicehub_backend.dto.ResidentBookingDTO;
import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.service.BookingService;
import com.servicehub.servicehub_backend.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public BookingController(final BookingService bookingService, final SimpMessagingTemplate messagingTemplate) {
        this.bookingService = bookingService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("")
    public ResponseEntity<StandardResponse<BookingDTO>> createBooking(@RequestBody BookingDTO bookingDTO) {
        logger.debug("Creating booking with details: {}", bookingDTO);
        String residentEmail = AuthUtil.getAuthenticatedUserEmail();

        try {
            logger.info("Creating booking with details for resident email: {}", residentEmail);
            BookingDTO createdBooking = bookingService.createBooking(residentEmail, bookingDTO);
            if (createdBooking != null) {
                String message = "New booking request: " + createdBooking.getStatus();
                messagingTemplate.convertAndSend("/topic/notifications", new NotificationDTO(message));
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new StandardResponse<>(true, "Booking created successfully", createdBooking));
            } else {
                logger.error("Booking creation failed for resident email: {}", residentEmail);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new StandardResponse<>(false, "Booking creation failed", null));
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating booking for resident email: {}", residentEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error creating booking", null));
        }
    }

    @PutMapping("/provider/accept/{bookingId}")
    public ResponseEntity<StandardResponse<BookingProviderMappingDTO>> acceptBooking(
            @PathVariable Long bookingId) {
        String providerEmail = AuthUtil.getAuthenticatedUserEmail();
        try {
            logger.info("Provider with email {} is accepting the booking with ID {}", providerEmail, bookingId);

            BookingProviderMappingDTO updatedBooking = bookingService.acceptBookingByProvider(bookingId, providerEmail);

            logger.info("Booking with ID {} accepted by provider {}", bookingId, providerEmail);
            return ResponseEntity.ok(new StandardResponse<>(true, "Booking accepted successfully", updatedBooking));
        } catch (RuntimeException e) {
            logger.error("Error accepting booking with ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error occurred while accepting booking with ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Internal server error", null));
        }
    }


    @PutMapping("/resident/accept/{bookingId}")
    public ResponseEntity<StandardResponse<BookingDTO>> confirmBooking(@PathVariable Long bookingId, @RequestParam Long providerId) {
        try {
            logger.info("Resident confirming the booking with ID {}", bookingId);
            BookingDTO responseConfirmationByResident = bookingService.confirmProviderByResident(bookingId, providerId);
            logger.info("Booking with ID {} accepted by resident", bookingId);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse<>(true, "Booking accepted successfully", responseConfirmationByResident));
        } catch (Exception e) {
            logger.error("Unexpected error occurred while accepting booking with id {}: {}", bookingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(false, "Internal server error", null));
        }
    }

    @GetMapping("/resident/count")
    public ResponseEntity<StandardResponse<List<BookingDTO>>> getResidentBookingCounts(@RequestParam(value = "status", required = false) String status) {
        String residentEmail = AuthUtil.getAuthenticatedUserEmail();

        try {
            List<BookingDTO> bookings = bookingService.getResidentBookingCountsByStatus(residentEmail, status);
            return ResponseEntity.ok(new StandardResponse<>(true, "Resident bookings fetched successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error fetching booking counts", null));
        }
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<StandardResponse<List<BookingDTO>>> getBookingsByStatus(@PathVariable String status) {
        String residentEmail = AuthUtil.getAuthenticatedUserEmail();

        try {
            List<BookingDTO> bookings = bookingService.getBookingsByStatus(residentEmail, status);
            return ResponseEntity.ok(new StandardResponse<>(true, "Bookings retrieved", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error fetching bookings", null));
        }
    }

    @GetMapping("/provider/count")
    public ResponseEntity<StandardResponse<List<ProviderBookingDTO>>> getProviderBookingCounts(@RequestParam(value = "status", required = false) String status) {
        String residentEmail = AuthUtil.getAuthenticatedUserEmail();

        try {
            List<ProviderBookingDTO> bookings = bookingService.getProviderBookingCountsByStatus(residentEmail, status);
            return ResponseEntity.ok(new StandardResponse<>(true, "Resident bookings fetched successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse<>(false, "Error fetching booking counts", null));
        }
    }
}
