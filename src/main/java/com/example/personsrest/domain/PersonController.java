package com.example.personsrest.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons/")
public class PersonController {
    PersonService personService;

    @GetMapping
    public List<PersonDTO> all(){
        return null; //personService.findAll();
    }
}
