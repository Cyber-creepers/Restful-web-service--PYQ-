package com.question_bank_backend.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdminEntity, String> {


    Optional<SuperAdminEntity> findByEmail(String email);

    Optional<SuperAdminEntity> findByName(String name);

}
