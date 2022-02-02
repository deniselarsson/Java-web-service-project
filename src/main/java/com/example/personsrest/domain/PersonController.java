package com.example.personsrest.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/persons/")
public class PersonController {
    List<String> person = new ArrayList<>();
    @GetMapping
    public CreatePerson createPerson() {
        CreatePerson createPerson = new CreatePerson("Arne Anka", "Ankeborg", 5);
        person.add(String.valueOf(createPerson));
        return createPerson;
    }

    @GetMapping("/testperson")
    public String getPerson() {
        return person.get(0);
    }
}
