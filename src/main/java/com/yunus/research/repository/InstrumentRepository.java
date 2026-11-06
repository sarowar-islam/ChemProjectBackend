package com.yunus.research.repository;

import com.yunus.research.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    Optional<Instrument> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
