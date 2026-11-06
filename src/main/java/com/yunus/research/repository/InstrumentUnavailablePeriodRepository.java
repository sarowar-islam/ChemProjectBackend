package com.yunus.research.repository;

import com.yunus.research.entity.InstrumentUnavailablePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstrumentUnavailablePeriodRepository extends JpaRepository<InstrumentUnavailablePeriod, String> {
    List<InstrumentUnavailablePeriod> findByInstrumentId(String instrumentId);

    @Query("SELECT p FROM InstrumentUnavailablePeriod p WHERE p.instrumentId = :instrumentId " +
           "AND ((p.startAt < :endAt AND p.endAt > :startAt))")
    List<InstrumentUnavailablePeriod> findConflictingPeriods(
        @Param("instrumentId") String instrumentId,
        @Param("startAt") LocalDateTime startAt,
        @Param("endAt") LocalDateTime endAt
    );

    void deleteByInstrumentId(String instrumentId);
}
