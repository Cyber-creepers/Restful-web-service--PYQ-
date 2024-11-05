package com.question_bank_backend.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {


    Optional<AdminEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
