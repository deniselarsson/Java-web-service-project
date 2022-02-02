package com.example.personsrest.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PersonService {
    PersonRepository personRepository;

    public List<PersonImpl> findAll(){
        return personRepository.findAll();
    }

    List<PersonImpl> person = new ArrayList<>();
    /*@GetMapping
    public PersonDTO createPerson() {
        PersonDTO personDTO = new PersonDTO("Arne Anka", "Ankeborg", 5);
        personService.add((personDTO));
        return personDTO;
    }

    private void add(PersonDTO personDTO) {
    }

    @GetMapping("/testperson")
    public PersonDTO getPerson() {
        return person.get(0);
    }*/
}
