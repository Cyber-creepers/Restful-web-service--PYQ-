package com.question_bank_backend.person;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImp implements PersonService
{

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    PersonServiceImp(PersonRepository personRepository,ObjectMapper objectMapper){
        this.personRepository=personRepository;
        this.objectMapper=objectMapper;
    }

}
