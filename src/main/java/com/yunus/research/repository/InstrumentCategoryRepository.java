package com.yunus.research.repository;

import com.yunus.research.entity.InstrumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentCategoryRepository extends JpaRepository<InstrumentCategory, String> {
}
