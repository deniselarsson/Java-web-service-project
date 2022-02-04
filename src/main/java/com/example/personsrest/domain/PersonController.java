package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private static PersonDTO toDTO(PersonEntity personEntity) {
        return new PersonDTO(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getCity(),
                personEntity.getAge()
        );
    }
}
