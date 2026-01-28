package com.yunus.research.repository;

import com.yunus.research.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
