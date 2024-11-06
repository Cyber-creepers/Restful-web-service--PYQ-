package com.question_bank_backend.activity;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImp implements ActivityService
{

    private final ActivityRepository activityRepository;
    private final ObjectMapper objectMapper;

    ActivityServiceImp(ActivityRepository activityRepository,ObjectMapper objectMapper){
        this.activityRepository = activityRepository;
        this.objectMapper= objectMapper;
    }


}
