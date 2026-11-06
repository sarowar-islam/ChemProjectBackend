package com.yunus.research.repository;

import com.yunus.research.entity.InstrumentSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentSpecificationRepository extends JpaRepository<InstrumentSpecification, String> {
    List<InstrumentSpecification> findByInstrumentIdOrderBySortOrder(String instrumentId);
    void deleteByInstrumentId(String instrumentId);
}
