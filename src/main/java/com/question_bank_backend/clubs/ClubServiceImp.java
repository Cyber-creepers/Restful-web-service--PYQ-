package com.question_bank_backend.clubs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImp implements ClubService
{

    private final ClubRepository clubRepository;
    private final ObjectMapper objectMapper;

    ClubServiceImp(ClubRepository clubRepository, ObjectMapper objectMapper){
        this.clubRepository= clubRepository;
        this.objectMapper = objectMapper;
    }



}
