package com.question_bank_backend.activity;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class ActivityController
{

    private final ActivityService activityService;

    ActivityController(ActivityService activityService){
        this.activityService = activityService;
    }
}
