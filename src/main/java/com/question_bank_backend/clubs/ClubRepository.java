package com.question_bank_backend.clubs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClubRepository  extends JpaRepository<ClubEntity, String>
{


}
