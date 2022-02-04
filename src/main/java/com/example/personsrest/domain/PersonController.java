package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons/")
@AllArgsConstructor
public class PersonController {

    PersonService personService;

    @GetMapping
    public List<PersonDTO> findAll() {
        return personService.findAll()
                .map(PersonController::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody CreatePerson createPerson) {
        return toDTO(personService.createPerson(
                createPerson.getName(),
                createPerson.getAge(),
                createPerson.getCity()));
    }

    private static PersonDTO toDTO(PersonEntity personEntity) {
        return new PersonDTO(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getCity(),
                personEntity.getAge()
        );
    }
}
