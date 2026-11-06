package com.yunus.research.repository;

import com.yunus.research.entity.InstrumentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentReviewRepository extends JpaRepository<InstrumentReview, String> {
    List<InstrumentReview> findByInstrumentIdOrderByCreatedAtDesc(String instrumentId);

    @Query("SELECT AVG(r.rating) FROM InstrumentReview r WHERE r.instrumentId = :instrumentId")
    Double getAverageRating(@Param("instrumentId") String instrumentId);

    @Query("SELECT COUNT(r) FROM InstrumentReview r WHERE r.instrumentId = :instrumentId")
    long countByInstrumentId(@Param("instrumentId") String instrumentId);

    void deleteByInstrumentId(String instrumentId);
}
