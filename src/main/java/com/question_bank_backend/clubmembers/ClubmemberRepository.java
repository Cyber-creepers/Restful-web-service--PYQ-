package com.question_bank_backend.clubmembers;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubmemberRepository extends JpaRepository<ClubmemberEntity,String>
{


}
