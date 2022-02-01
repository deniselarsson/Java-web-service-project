package com.example.personsrest.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @GetMapping
    public PersonTest getPerson(){
        return new PersonTest("1", "michelle", 22, "Malmö", "3");
    }
}
