package com.yunus.research.repository;

import com.yunus.research.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, String> {
    List<Publication> findByMemberId(String memberId);

    List<Publication> findAllByOrderByYearDesc();
}
