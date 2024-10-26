package com.question_bank_backend.clubmembers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ClubmemberServiceImp implements  ClubmemberService
{

    private ClubmemberRepository clubmemberRepository;
    private ObjectMapper objectMapper;

    ClubmemberServiceImp(ClubmemberRepository clubmemberRepository, ObjectMapper objectMapper){

        this.clubmemberRepository=clubmemberRepository;
        this.objectMapper=objectMapper;
    }



}
