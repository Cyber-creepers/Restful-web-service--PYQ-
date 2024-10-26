package com.question_bank_backend.clubs;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class ClubController
{


    private ClubService clubService;

    ClubController(ClubService clubService){
        this.clubService=clubService;
    }


}
