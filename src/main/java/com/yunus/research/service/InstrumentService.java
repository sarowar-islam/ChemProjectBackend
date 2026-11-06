package com.yunus.research.service;

import com.yunus.research.dto.*;
import com.yunus.research.entity.*;
import com.yunus.research.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentSpecificationRepository specificationRepository;
    private final InstrumentBookingRepository bookingRepository;
    private final InstrumentReviewRepository reviewRepository;
    private final InstrumentUnavailablePeriodRepository unavailablePeriodRepository;
    private final InstrumentCategoryRepository categoryRepository;

    // ==================== Instrument CRUD ====================

    public List<InstrumentDto> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public InstrumentDto getInstrumentBySlug(String slug) {
        Instrument instrument = instrumentRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
        return toDto(instrument);
    }

    public InstrumentDto getInstrumentById(String id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
        return toDto(instrument);
    }

    @Transactional
    public InstrumentDto createInstrument(CreateInstrumentRequest request) {
        // Generate unique slug
        String slug = createSlug(request.getName());
        if (instrumentRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Instrument instrument = new Instrument();
        instrument.setSlug(slug);
        instrument.setName(request.getName());
        instrument.setShortDescription(request.getShortDescription());
        instrument.setDescription(request.getDescription());
        instrument.setCategoryId(request.getCategoryId());
        instrument.setCategory(request.getCategory());
        instrument.setStatus(request.getStatus());
        instrument.setImageUrl(request.getImageUrl());
        instrument.setManufacturer(request.getManufacturer());
        instrument.setModel(request.getModel());
        instrument.setSerialNumber(request.getSerialNumber());
        instrument.setLocation(request.getLocation());
        instrument.setResponsibleAdminId(request.getResponsibleAdminId());
        instrument.setResponsibleAdminName(request.getResponsibleAdminName());
        instrument.setResponsibleAdminEmail(request.getResponsibleAdminEmail());
        instrument.setFeatures(request.getFeatures() != null ? request.getFeatures() : new ArrayList<>());
        instrument.setUsageRequirements(request.getUsageRequirements() != null ? request.getUsageRequirements() : new ArrayList<>());
        instrument.setOpeningHour(request.getOpeningHour());
        instrument.setClosingHour(request.getClosingHour());
        instrument.setSlotIntervalMinutes(request.getSlotIntervalMinutes());
        instrument.setMinDurationMinutes(request.getMinDurationMinutes());
        instrument.setMaxDurationMinutes(request.getMaxDurationMinutes());
        instrument.setAdvanceBookingDays(request.getAdvanceBookingDays());

        Instrument savedInstrument = instrumentRepository.save(instrument);

        // Save specifications
        if (request.getSpecifications() != null) {
            for (int i = 0; i < request.getSpecifications().size(); i++) {
                CreateInstrumentRequest.SpecificationInput specInput = request.getSpecifications().get(i);
                InstrumentSpecification spec = new InstrumentSpecification();
                spec.setInstrumentId(savedInstrument.getId());
                spec.setLabel(specInput.getLabel());
                spec.setValue(specInput.getValue());
                spec.setSortOrder(i);
                specificationRepository.save(spec);
            }
        }

        return toDto(savedInstrument);
    }

    @Transactional
    public InstrumentDto updateInstrument(String id, CreateInstrumentRequest request) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));

        instrument.setName(request.getName());
        instrument.setShortDescription(request.getShortDescription());
        instrument.setDescription(request.getDescription());
        instrument.setCategoryId(request.getCategoryId());
        instrument.setCategory(request.getCategory());
        instrument.setStatus(request.getStatus());
        instrument.setImageUrl(request.getImageUrl());
        instrument.setManufacturer(request.getManufacturer());
        instrument.setModel(request.getModel());
        instrument.setSerialNumber(request.getSerialNumber());
        instrument.setLocation(request.getLocation());
        instrument.setResponsibleAdminId(request.getResponsibleAdminId());
        instrument.setResponsibleAdminName(request.getResponsibleAdminName());
        instrument.setResponsibleAdminEmail(request.getResponsibleAdminEmail());
        instrument.setFeatures(request.getFeatures() != null ? request.getFeatures() : new ArrayList<>());
        instrument.setUsageRequirements(request.getUsageRequirements() != null ? request.getUsageRequirements() : new ArrayList<>());
        instrument.setOpeningHour(request.getOpeningHour());
        instrument.setClosingHour(request.getClosingHour());
        instrument.setSlotIntervalMinutes(request.getSlotIntervalMinutes());
        instrument.setMinDurationMinutes(request.getMinDurationMinutes());
        instrument.setMaxDurationMinutes(request.getMaxDurationMinutes());
        instrument.setAdvanceBookingDays(request.getAdvanceBookingDays());

        Instrument updatedInstrument = instrumentRepository.save(instrument);

        // Update specifications
        if (request.getSpecifications() != null) {
            specificationRepository.deleteByInstrumentId(id);
            for (int i = 0; i < request.getSpecifications().size(); i++) {
                CreateInstrumentRequest.SpecificationInput specInput = request.getSpecifications().get(i);
                InstrumentSpecification spec = new InstrumentSpecification();
                spec.setInstrumentId(id);
                spec.setLabel(specInput.getLabel());
                spec.setValue(specInput.getValue());
                spec.setSortOrder(i);
                specificationRepository.save(spec);
            }
        }

        return toDto(updatedInstrument);
    }

    @Transactional
    public InstrumentDto updateInstrumentStatus(String id, String status) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
        instrument.setStatus(status);
        Instrument updated = instrumentRepository.save(instrument);
        return toDto(updated);
    }

    @Transactional
    public void deleteInstrument(String id) {
        if (!instrumentRepository.existsById(id)) {
            throw new RuntimeException("Instrument not found");
        }
        specificationRepository.deleteByInstrumentId(id);
        bookingRepository.deleteByInstrumentId(id);
        reviewRepository.deleteByInstrumentId(id);
        unavailablePeriodRepository.deleteByInstrumentId(id);
        instrumentRepository.deleteById(id);
    }

    // ==================== Categories ====================

    public List<InstrumentCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstrumentCategoryDto createCategory(String name) {
        InstrumentCategory category = new InstrumentCategory();
        category.setName(name);
        InstrumentCategory saved = categoryRepository.save(category);
        return toCategoryDto(saved);
    }

    // ==================== Bookings ====================

    public List<InstrumentBookingDto> getAllBookings(String status) {
        List<InstrumentBooking> bookings;
        if (status != null && !status.isEmpty()) {
            bookings = bookingRepository.findByStatus(status);
        } else {
            bookings = bookingRepository.findAll();
        }
        return bookings.stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }

    public InstrumentBookingDto getBookingById(String id) {
        InstrumentBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return toBookingDto(booking);
    }

    public long getPendingBookingsCount() {
        return bookingRepository.countPendingBookings();
    }

    @Transactional
    public InstrumentBookingDto createBooking(CreateBookingRequest request) {
        // Validate time
        if (request.getEndAt().isBefore(request.getStartAt()) || request.getEndAt().isEqual(request.getStartAt())) {
            throw new RuntimeException("End time must be after start time");
        }

        // Check for conflicts
        String conflict = checkBookingConflict(request.getInstrumentId(), request.getStartAt(), request.getEndAt(), null);
        if (conflict != null) {
            throw new RuntimeException(conflict);
        }

        InstrumentBooking booking = new InstrumentBooking();
        booking.setInstrumentId(request.getInstrumentId());
        booking.setRequesterId(request.getRequesterId());
        booking.setRequesterName(request.getRequesterName());
        booking.setRequesterEmail(request.getRequesterEmail());
        booking.setRequesterPhone(request.getRequesterPhone());
        booking.setAffiliation(request.getAffiliation());
        booking.setSupervisorName(request.getSupervisorName());
        booking.setPurposeTitle(request.getPurposeTitle());
        booking.setPurposeDescription(request.getPurposeDescription());
        booking.setSampleDetails(request.getSampleDetails());
        booking.setStartAt(request.getStartAt());
        booking.setEndAt(request.getEndAt());
        booking.setStatus("pending");

        InstrumentBooking saved = bookingRepository.save(booking);
        return toBookingDto(saved);
    }

    @Transactional
    public InstrumentBookingDto approveBooking(String id, String reviewerId, String adminNote) {
        InstrumentBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check for conflicts before approving
        String conflict = checkBookingConflict(booking.getInstrumentId(), booking.getStartAt(), booking.getEndAt(), id);
        if (conflict != null) {
            throw new RuntimeException(conflict);
        }

        booking.setStatus("approved");
        booking.setAdminNote(adminNote);
        booking.setReviewedBy(reviewerId);
        booking.setReviewedAt(LocalDateTime.now());

        InstrumentBooking updated = bookingRepository.save(booking);
        return toBookingDto(updated);
    }

    @Transactional
    public InstrumentBookingDto rejectBooking(String id, String reviewerId, String adminNote) {
        InstrumentBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (adminNote == null || adminNote.trim().isEmpty()) {
            throw new RuntimeException("Admin note is required for rejection");
        }

        booking.setStatus("rejected");
        booking.setAdminNote(adminNote);
        booking.setReviewedBy(reviewerId);
        booking.setReviewedAt(LocalDateTime.now());

        InstrumentBooking updated = bookingRepository.save(booking);
        return toBookingDto(updated);
    }

    @Transactional
    public InstrumentBookingDto cancelBooking(String id, String reviewerId, String adminNote) {
        InstrumentBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("cancelled");
        booking.setAdminNote(adminNote);
        booking.setReviewedBy(reviewerId);
        booking.setReviewedAt(LocalDateTime.now());

        InstrumentBooking updated = bookingRepository.save(booking);
        return toBookingDto(updated);
    }

    private String checkBookingConflict(String instrumentId, LocalDateTime startAt, LocalDateTime endAt, String ignoreBookingId) {
        // Check approved bookings
        List<InstrumentBooking> conflictingBookings = bookingRepository.findConflictingBookings(instrumentId, startAt, endAt);
        if (ignoreBookingId != null) {
            conflictingBookings = conflictingBookings.stream()
                    .filter(b -> !b.getId().equals(ignoreBookingId))
                    .collect(Collectors.toList());
        }
        if (!conflictingBookings.isEmpty()) {
            return "This time overlaps with an existing approved reservation.";
        }

        // Check unavailable periods
        List<InstrumentUnavailablePeriod> conflictingPeriods = unavailablePeriodRepository.findConflictingPeriods(instrumentId, startAt, endAt);
        if (!conflictingPeriods.isEmpty()) {
            InstrumentUnavailablePeriod period = conflictingPeriods.get(0);
            return "The instrument is unavailable during this period (" + period.getReason() + ").";
        }

        return null;
    }

    // ==================== Calendar ====================

    public List<InstrumentCalendarEventDto> getCalendarEvents(String instrumentId) {
        List<InstrumentCalendarEventDto> events = new ArrayList<>();

        // Add approved bookings
        List<InstrumentBooking> bookings = bookingRepository.findByInstrumentIdAndStatus(instrumentId, "approved");
        for (InstrumentBooking booking : bookings) {
            InstrumentCalendarEventDto event = new InstrumentCalendarEventDto();
            event.setId(booking.getId());
            event.setInstrumentId(instrumentId);
            event.setKind("booking");
            event.setTitle("Reserved");
            event.setSubtitle(booking.getRequesterName());
            event.setStartAt(booking.getStartAt());
            event.setEndAt(booking.getEndAt());
            events.add(event);
        }

        // Add unavailable periods
        List<InstrumentUnavailablePeriod> periods = unavailablePeriodRepository.findByInstrumentId(instrumentId);
        for (InstrumentUnavailablePeriod period : periods) {
            InstrumentCalendarEventDto event = new InstrumentCalendarEventDto();
            event.setId(period.getId());
            event.setInstrumentId(instrumentId);
            event.setKind("unavailable");
            event.setTitle(getUnavailableTitle(period.getType()));
            event.setSubtitle(period.getReason());
            event.setStartAt(period.getStartAt());
            event.setEndAt(period.getEndAt());
            events.add(event);
        }

        return events;
    }

    private String getUnavailableTitle(String type) {
        return switch (type) {
            case "maintenance" -> "Maintenance";
            case "calibration" -> "Calibration";
            case "closed" -> "Closed";
            default -> "Unavailable";
        };
    }

    // ==================== Reviews ====================

    public List<InstrumentReviewDto> getReviews(String instrumentId) {
        return reviewRepository.findByInstrumentIdOrderByCreatedAtDesc(instrumentId).stream()
                .map(this::toReviewDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstrumentReviewDto createReview(CreateReviewRequest request) {
        InstrumentReview review = new InstrumentReview();
        review.setInstrumentId(request.getInstrumentId());
        review.setBookingId(request.getBookingId());
        review.setReviewerName(request.getReviewerName());
        review.setReviewerAffiliation(request.getReviewerAffiliation());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        InstrumentReview saved = reviewRepository.save(review);
        return toReviewDto(saved);
    }

    // ==================== Unavailable Periods ====================

    @Transactional
    public InstrumentUnavailablePeriodDto createUnavailablePeriod(InstrumentUnavailablePeriodDto dto) {
        InstrumentUnavailablePeriod period = new InstrumentUnavailablePeriod();
        period.setInstrumentId(dto.getInstrumentId());
        period.setStartAt(dto.getStartAt());
        period.setEndAt(dto.getEndAt());
        period.setReason(dto.getReason());
        period.setType(dto.getType());

        InstrumentUnavailablePeriod saved = unavailablePeriodRepository.save(period);
        return toUnavailablePeriodDto(saved);
    }

    @Transactional
    public void deleteUnavailablePeriod(String id) {
        unavailablePeriodRepository.deleteById(id);
    }

    // ==================== Helper Methods ====================

    private String createSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    private InstrumentDto toDto(Instrument instrument) {
        InstrumentDto dto = new InstrumentDto();
        dto.setId(instrument.getId());
        dto.setSlug(instrument.getSlug());
        dto.setName(instrument.getName());
        dto.setShortDescription(instrument.getShortDescription());
        dto.setDescription(instrument.getDescription());
        dto.setCategoryId(instrument.getCategoryId());
        dto.setCategory(instrument.getCategory());
        dto.setStatus(instrument.getStatus());
        dto.setImageUrl(instrument.getImageUrl());
        dto.setManufacturer(instrument.getManufacturer());
        dto.setModel(instrument.getModel());
        dto.setSerialNumber(instrument.getSerialNumber());
        dto.setLocation(instrument.getLocation());
        dto.setResponsibleAdminId(instrument.getResponsibleAdminId());
        dto.setResponsibleAdminName(instrument.getResponsibleAdminName());
        dto.setResponsibleAdminEmail(instrument.getResponsibleAdminEmail());
        dto.setFeatures(instrument.getFeatures());
        dto.setUsageRequirements(instrument.getUsageRequirements());
        dto.setOpeningHour(instrument.getOpeningHour());
        dto.setClosingHour(instrument.getClosingHour());
        dto.setSlotIntervalMinutes(instrument.getSlotIntervalMinutes());
        dto.setMinDurationMinutes(instrument.getMinDurationMinutes());
        dto.setMaxDurationMinutes(instrument.getMaxDurationMinutes());
        dto.setAdvanceBookingDays(instrument.getAdvanceBookingDays());
        dto.setCreatedAt(instrument.getCreatedAt());
        dto.setUpdatedAt(instrument.getUpdatedAt());

        // Load specifications
        List<InstrumentSpecification> specs = specificationRepository.findByInstrumentIdOrderBySortOrder(instrument.getId());
        dto.setSpecifications(specs.stream().map(this::toSpecificationDto).collect(Collectors.toList()));

        // Load rating
        Double avgRating = reviewRepository.getAverageRating(instrument.getId());
        dto.setAverageRating(avgRating != null ? avgRating : 0.0);
        dto.setReviewCount(reviewRepository.countByInstrumentId(instrument.getId()));

        return dto;
    }

    private InstrumentSpecificationDto toSpecificationDto(InstrumentSpecification spec) {
        InstrumentSpecificationDto dto = new InstrumentSpecificationDto();
        dto.setId(spec.getId());
        dto.setInstrumentId(spec.getInstrumentId());
        dto.setLabel(spec.getLabel());
        dto.setValue(spec.getValue());
        dto.setSortOrder(spec.getSortOrder());
        return dto;
    }

    private InstrumentBookingDto toBookingDto(InstrumentBooking booking) {
        InstrumentBookingDto dto = new InstrumentBookingDto();
        dto.setId(booking.getId());
        dto.setInstrumentId(booking.getInstrumentId());
        dto.setRequesterId(booking.getRequesterId());
        dto.setRequesterName(booking.getRequesterName());
        dto.setRequesterEmail(booking.getRequesterEmail());
        dto.setRequesterPhone(booking.getRequesterPhone());
        dto.setAffiliation(booking.getAffiliation());
        dto.setSupervisorName(booking.getSupervisorName());
        dto.setPurposeTitle(booking.getPurposeTitle());
        dto.setPurposeDescription(booking.getPurposeDescription());
        dto.setSampleDetails(booking.getSampleDetails());
        dto.setStartAt(booking.getStartAt());
        dto.setEndAt(booking.getEndAt());
        dto.setStatus(booking.getStatus());
        dto.setAdminNote(booking.getAdminNote());
        dto.setReviewedBy(booking.getReviewedBy());
        dto.setReviewedAt(booking.getReviewedAt());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }

    private InstrumentReviewDto toReviewDto(InstrumentReview review) {
        InstrumentReviewDto dto = new InstrumentReviewDto();
        dto.setId(review.getId());
        dto.setInstrumentId(review.getInstrumentId());
        dto.setBookingId(review.getBookingId());
        dto.setReviewerName(review.getReviewerName());
        dto.setReviewerAffiliation(review.getReviewerAffiliation());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private InstrumentCategoryDto toCategoryDto(InstrumentCategory category) {
        InstrumentCategoryDto dto = new InstrumentCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    private InstrumentUnavailablePeriodDto toUnavailablePeriodDto(InstrumentUnavailablePeriod period) {
        InstrumentUnavailablePeriodDto dto = new InstrumentUnavailablePeriodDto();
        dto.setId(period.getId());
        dto.setInstrumentId(period.getInstrumentId());
        dto.setStartAt(period.getStartAt());
        dto.setEndAt(period.getEndAt());
        dto.setReason(period.getReason());
        dto.setType(period.getType());
        return dto;
    }
}
