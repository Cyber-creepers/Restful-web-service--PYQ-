package com.question_bank_backend.person;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class PersonController
{

    private final PersonService personService;

    PersonController(PersonService personService)
    {
        this.personService=personService;
    }



}
