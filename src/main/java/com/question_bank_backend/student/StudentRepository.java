package com.question_bank_backend.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String>
{

    Optional<StudentEntity> findByEmail(String email);

}
