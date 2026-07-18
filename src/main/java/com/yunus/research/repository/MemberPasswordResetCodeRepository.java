package com.yunus.research.repository;

import com.yunus.research.entity.MemberPasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberPasswordResetCodeRepository extends JpaRepository<MemberPasswordResetCode, String> {
    Optional<MemberPasswordResetCode> findTopByEmailIgnoreCaseAndUsedAtIsNullOrderByCreatedAtDesc(String email);

    void deleteByEmailIgnoreCase(String email);
}