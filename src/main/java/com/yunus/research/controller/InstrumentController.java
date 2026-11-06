package com.yunus.research.controller;

import com.yunus.research.dto.*;
import com.yunus.research.service.InstrumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;

    // ==================== Public Endpoints ====================

    @GetMapping("/instruments")
    public ResponseEntity<Map<String, Object>> getAllInstruments() {
        try {
            List<InstrumentDto> instruments = instrumentService.getAllInstruments();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", instruments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/instruments/slug/{slug}")
    public ResponseEntity<Map<String, Object>> getInstrumentBySlug(@PathVariable String slug) {
        try {
            InstrumentDto instrument = instrumentService.getInstrumentBySlug(slug);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", instrument);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/instrument-categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        try {
            List<InstrumentCategoryDto> categories = instrumentService.getAllCategories();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", categories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/instruments/{id}/calendar")
    public ResponseEntity<Map<String, Object>> getCalendarEvents(@PathVariable String id) {
        try {
            List<InstrumentCalendarEventDto> events = instrumentService.getCalendarEvents(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", events);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/instruments/{id}/reviews")
    public ResponseEntity<Map<String, Object>> getReviews(@PathVariable String id) {
        try {
            List<InstrumentReviewDto> reviews = instrumentService.getReviews(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reviews);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping("/instrument-bookings")
    public ResponseEntity<Map<String, Object>> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        try {
            InstrumentBookingDto booking = instrumentService.createBooking(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping("/instrument-reviews")
    public ResponseEntity<Map<String, Object>> createReview(@Valid @RequestBody CreateReviewRequest request) {
        try {
            InstrumentReviewDto review = instrumentService.createReview(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", review);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ==================== Admin Endpoints ====================

    @PostMapping("/admin/instruments")
    public ResponseEntity<Map<String, Object>> createInstrument(@Valid @RequestBody CreateInstrumentRequest request) {
        try {
            InstrumentDto instrument = instrumentService.createInstrument(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", instrument);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PutMapping("/admin/instruments/{id}")
    public ResponseEntity<Map<String, Object>> updateInstrument(
            @PathVariable String id,
            @Valid @RequestBody CreateInstrumentRequest request) {
        try {
            InstrumentDto instrument = instrumentService.updateInstrument(id, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", instrument);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PatchMapping("/admin/instruments/{id}/status")
    public ResponseEntity<Map<String, Object>> updateInstrumentStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            if (status == null || status.isEmpty()) {
                return createErrorResponse("Status is required");
            }
            InstrumentDto instrument = instrumentService.updateInstrumentStatus(id, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", instrument);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @DeleteMapping("/admin/instruments/{id}")
    public ResponseEntity<Map<String, Object>> deleteInstrument(@PathVariable String id) {
        try {
            instrumentService.deleteInstrument(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/admin/instrument-bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings(@RequestParam(required = false) String status) {
        try {
            List<InstrumentBookingDto> bookings = instrumentService.getAllBookings(status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/admin/instrument-bookings/pending-count")
    public ResponseEntity<Map<String, Object>> getPendingCount() {
        try {
            long count = instrumentService.getPendingBookingsCount();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("count", count));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @GetMapping("/admin/instrument-bookings/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable String id) {
        try {
            InstrumentBookingDto booking = instrumentService.getBookingById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PatchMapping("/admin/instrument-bookings/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveBooking(
            @PathVariable String id,
            @RequestBody BookingActionRequest request) {
        try {
            // In a real app, get reviewer ID from auth token
            String reviewerId = "admin-001";
            InstrumentBookingDto booking = instrumentService.approveBooking(id, reviewerId, request.getAdminNote());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PatchMapping("/admin/instrument-bookings/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectBooking(
            @PathVariable String id,
            @RequestBody BookingActionRequest request) {
        try {
            if (request.getAdminNote() == null || request.getAdminNote().trim().isEmpty()) {
                return createErrorResponse("Admin note is required for rejection");
            }
            // In a real app, get reviewer ID from auth token
            String reviewerId = "admin-001";
            InstrumentBookingDto booking = instrumentService.rejectBooking(id, reviewerId, request.getAdminNote());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @DeleteMapping("/admin/instrument-bookings/{id}")
    public ResponseEntity<Map<String, Object>> cancelBooking(
            @PathVariable String id,
            @RequestBody(required = false) BookingActionRequest request) {
        try {
            // In a real app, get reviewer ID from auth token
            String reviewerId = "admin-001";
            String adminNote = request != null ? request.getAdminNote() : null;
            InstrumentBookingDto booking = instrumentService.cancelBooking(id, reviewerId, adminNote);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping("/admin/instrument-categories")
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            if (name == null || name.isEmpty()) {
                return createErrorResponse("Category name is required");
            }
            InstrumentCategoryDto category = instrumentService.createCategory(name);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", category);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PostMapping("/admin/instrument-unavailable-periods")
    public ResponseEntity<Map<String, Object>> createUnavailablePeriod(@RequestBody InstrumentUnavailablePeriodDto dto) {
        try {
            InstrumentUnavailablePeriodDto period = instrumentService.createUnavailablePeriod(dto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", period);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @DeleteMapping("/admin/instrument-unavailable-periods/{id}")
    public ResponseEntity<Map<String, Object>> deleteUnavailablePeriod(@PathVariable String id) {
        try {
            instrumentService.deleteUnavailablePeriod(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ==================== Helper Methods ====================

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.badRequest().body(response);
    }
}
