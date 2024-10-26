package com.question_bank_backend.clubmembers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class ClubmemberController
{


    private ClubmemberService clubmemberService;
    ClubmemberController(ClubmemberService clubmemberService){
        this.clubmemberService = clubmemberService;
    }


}
