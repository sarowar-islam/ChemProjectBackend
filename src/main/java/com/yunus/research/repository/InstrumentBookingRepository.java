package com.yunus.research.repository;

import com.yunus.research.entity.InstrumentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstrumentBookingRepository extends JpaRepository<InstrumentBooking, String> {
    List<InstrumentBooking> findByInstrumentIdAndStatus(String instrumentId, String status);
    List<InstrumentBooking> findByStatus(String status);
    List<InstrumentBooking> findByInstrumentIdOrderByCreatedAtDesc(String instrumentId);

    @Query("SELECT COUNT(b) FROM InstrumentBooking b WHERE b.status = 'pending'")
    long countPendingBookings();

    @Query("SELECT b FROM InstrumentBooking b WHERE b.instrumentId = :instrumentId " +
           "AND b.status = 'approved' " +
           "AND ((b.startAt < :endAt AND b.endAt > :startAt))")
    List<InstrumentBooking> findConflictingBookings(
        @Param("instrumentId") String instrumentId,
        @Param("startAt") LocalDateTime startAt,
        @Param("endAt") LocalDateTime endAt
    );

    void deleteByInstrumentId(String instrumentId);
}
