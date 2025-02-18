package com.servicehub.servicehub_backend.service;

import com.servicehub.servicehub_backend.adapter.BookingFormAdapter;
import com.servicehub.servicehub_backend.adapter.BookingProviderMappingAdapter;
import com.servicehub.servicehub_backend.dto.BookingDTO;
import com.servicehub.servicehub_backend.dto.BookingProviderMappingDTO;
import com.servicehub.servicehub_backend.constant.BookingStatus;
import com.servicehub.servicehub_backend.dto.ProviderBookingDTO;
import com.servicehub.servicehub_backend.model.BookingEntity;
import com.servicehub.servicehub_backend.model.BookingProviderMappingEntity;
import com.servicehub.servicehub_backend.model.ProviderInfoEntity;
import com.servicehub.servicehub_backend.model.ResidentInfoEntity;
import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import com.servicehub.servicehub_backend.repository.BookingRepository;
import com.servicehub.servicehub_backend.repository.BookingProviderMappingRepository;
import com.servicehub.servicehub_backend.repository.ProviderRepository;
import com.servicehub.servicehub_backend.repository.ResidentRepository;
import com.servicehub.servicehub_backend.repository.ServiceCategoryInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final ResidentRepository residentRepository;
    private final ProviderRepository providerRepository;
    private final ServiceCategoryInfoRepository serviceCategoryInfoRepository;
    private final BookingProviderMappingRepository bookingProviderMappingRepository;
    private final BookingFormAdapter bookingFormAdapter;
    private final BookingProviderMappingAdapter bookingProviderMappingAdapter;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ResidentRepository residentRepository, ProviderRepository providerRepository, ServiceCategoryInfoRepository serviceCategoryInfoRepository, BookingProviderMappingRepository bookingProviderMappingRepository, BookingFormAdapter bookingFormAdapter, BookingProviderMappingAdapter bookingProviderMappingAdapter) {
        this.bookingRepository = bookingRepository;
        this.residentRepository = residentRepository;
        this.providerRepository = providerRepository;
        this.serviceCategoryInfoRepository = serviceCategoryInfoRepository;
        this.bookingProviderMappingRepository = bookingProviderMappingRepository;
        this.bookingFormAdapter = bookingFormAdapter;
        this.bookingProviderMappingAdapter = bookingProviderMappingAdapter;
    }


    @Override
    public BookingDTO createBooking(String residentEmail, BookingDTO bookingDTO) {

        logger.info("Resident with email {} is creating a booking", residentEmail);

        ResidentInfoEntity resident = residentRepository.findByUser_Email(residentEmail)
                .orElseThrow(() -> new RuntimeException("Resident not found for id: " + residentEmail));

        ServiceCategoryInfoEntity serviceCategoryInfoEntity = serviceCategoryInfoRepository.findById(bookingDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found for id: " + bookingDTO.getServiceId()));

        if (resident == null) {
            logger.error("Resident not found for email: {}", residentEmail);
            throw new RuntimeException("Resident not found for email: " + residentEmail);
        }
        bookingDTO.setResidentId(resident.getId());
        bookingDTO.setStatus(BookingStatus.PENDING.name());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setResident(resident);
        bookingEntity.setService(serviceCategoryInfoEntity);
        bookingEntity.setTimeSlot(bookingDTO.getTimeSlot());
        bookingEntity.setBookingDate(bookingDTO.getBookingDate());
        bookingEntity.setAddress(bookingDTO.getAddress());
        bookingEntity.setStatus(BookingStatus.PENDING);

        bookingRepository.save(bookingEntity);

        logger.debug("Booking created successfully: {}", bookingEntity);
        logger.info("BookingProviderMapping saved with Booking ID: {}", bookingEntity.getId());

        return bookingFormAdapter.toDTO(bookingEntity);
    }

    @Override
    public BookingProviderMappingDTO acceptBookingByProvider(Long bookingId, String providerEmail) {
        logger.info("Processing provider acceptance for booking ID {} with email {}", bookingId, providerEmail);

        try {

            Optional<ProviderInfoEntity> provider = providerRepository.findByUser_Email(providerEmail);
            if (provider.isEmpty()) {
                logger.error("Provider not found for email: {}", providerEmail);
                throw new RuntimeException("Provider not found for email: " + providerEmail);
            }

            BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> {
                        logger.error("Booking not found for ID: {}", bookingId);
                        return new RuntimeException("Booking not found for ID: " + bookingId);
                    });

            if (!BookingStatus.PENDING.equals(bookingEntity.getStatus())) {
                logger.error("Booking with ID {} is not in a valid state for provider acceptance", bookingId);
                throw new RuntimeException("Booking with ID " + bookingId + " is not in a valid state for provider acceptance");
            }

            BookingProviderMappingEntity bookingProviderMapping = new BookingProviderMappingEntity();
            bookingProviderMapping.setBooking(bookingEntity);
            bookingProviderMapping.setProvider(provider.get());
            bookingProviderMapping.setStatus(BookingStatus.ACCEPTED.name());

            bookingProviderMappingRepository.save(bookingProviderMapping);

            logger.info("New booking created with ID {} for provider {}", bookingId, provider.get().getFullName());

            return bookingProviderMappingAdapter.toDTO(bookingProviderMapping);
        } catch (Exception e) {
            logger.error("Error occurred while processing provider acceptance for booking ID {}: {}", bookingId, e.getMessage());
            throw e;
        }
    }


    public BookingDTO confirmProviderByResident(Long bookingId, Long providerId) {

        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found for ID: " + bookingId));

        if (!BookingStatus.PENDING.equals(bookingEntity.getStatus()) &&
                !BookingStatus.ACCEPTED.equals(bookingEntity.getStatus())) {
            throw new RuntimeException("Booking with ID " + bookingId + " is not in a valid state for resident confirmation");
        }

        BookingProviderMappingEntity selectedMapping = bookingProviderMappingRepository
                .findByBooking_IdAndProvider_Id(bookingId, providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found for booking ID: " + bookingId));

        selectedMapping.setStatus(BookingStatus.COMPLETED.name());
        bookingProviderMappingRepository.save(selectedMapping);

        bookingEntity.setServiceProvider(selectedMapping.getProvider());
        bookingEntity.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(bookingEntity);

        return bookingFormAdapter.toDTO(bookingEntity);
    }

    @Override
    public List<BookingDTO> getResidentBookingCountsByStatus(String residentEmail, String status) {
        logger.info("Resident with email {} is creating a booking", residentEmail);

        ResidentInfoEntity resident = residentRepository.findByUser_Email(residentEmail)
                .orElseThrow(() -> new RuntimeException("Resident not found for id: " + residentEmail));

        List<BookingEntity> bookings = bookingRepository.findBookingsWithStatusByResident(resident.getId(), status);

        return bookings.stream().map(be -> new BookingDTO(
                be.getId(),
                be.getResident().getId(),
                be.getServiceProvider() != null ? be.getServiceProvider().getId() : null,
                be.getService().getId(),
                be.getService().getName(),
                be.getBookingDate(),
                be.getTimeSlot(),
                be.getAddress(),
                be.getAdditionalInfo(),  // Add missing additionalInfo
                be.getStatus().toString()
        )).collect(Collectors.toList());

    }

    @Override
    public List<ProviderBookingDTO> getProviderBookingCountsByStatus(String providerEmail, String status) {
        logger.info("Resident with email {} is creating a booking", providerEmail);

        ProviderInfoEntity provider = providerRepository.findByUser_Email(providerEmail)
                .orElseThrow(() -> new RuntimeException("Resident not found for id: " + providerEmail));

//        List<BookingProviderMappingEntity> bookings = bookingRepository.findBookingsWithStatusByProvider(provider.getId(), status);
        List<BookingProviderMappingEntity> bookings = bookingRepository.findBookingsWithStatusByProvider(provider.getId(), status);


        return bookings.stream().map(bpm -> new ProviderBookingDTO(
                bpm.getBooking().getId(),
                bpm.getBooking().getService().getName(),
                bpm.getBooking().getResident().getFullName(),
                bpm.getBooking().getBookingDate(),
                bpm.getBooking().getTimeSlot(),
                bpm.getBooking().getAddress(),
                bpm.getStatus()
        )).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByStatus(String residentEmail, String status) {
        return null;
    }

}

