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

    //TODO:
    //Anrop - hämta personer
    //Parameter - search
    //Filtrerar sökresultat med sökning på Förnamn och efternamn.Kan vara tom (null).
    //Parameter - PageNumber
    //Sidnummer för begränsning av sökresultat vidsökningar.Kan vara tom (null).
    //Parameter - PageSize
    //Max träffar per sida för begränsning av sökresultat vidsökningar.Kan vara tom (null).
    //Retur - Lista medPersoner


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

    @GetMapping("/{id}")
    public PersonDTO get(@PathVariable("id") String id) {
        return toDTO(personService.get(id));
    }

    @PutMapping("/{id}")
    public PersonDTO update(@PathVariable("id") String id, @RequestBody UpdatePerson updatePerson) {
        return toDTO(
                personService.updatePerson(
                        id,
                        updatePerson.getName(),
                        updatePerson.getAge(),
                        updatePerson.getCity()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        personService.delete(id);
    }

    private static PersonDTO toDTO(PersonEntity personEntity) {
        return new PersonDTO(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getCity(),
                personEntity.getAge()
        );
    }
    //TODO:
    //Anrop: Lägg till en grupp på en person
    //URI - /persons/[id]/addGroup
    //Metod - GET
    //Parameter - Namn, Namn på Gruppen som skall associeras med personen.
    //Retur - Person med det ID

    //TODO:
    //Anrop: /persons/[id]/removeGroup
    //URI - /persons/[id]/addGroup
    //Metod - GET
    //Parameter - Name, Namn på Gruppen som skall tas bort från personen.
    //Retur - Person med det ID
}
