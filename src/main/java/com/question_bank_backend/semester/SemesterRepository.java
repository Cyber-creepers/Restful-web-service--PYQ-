package com.question_bank_backend.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterEntity,String>
{


    SemesterEntity findBySemester(int semester);

}
