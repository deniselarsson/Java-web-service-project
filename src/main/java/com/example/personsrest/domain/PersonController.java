package com.example.personsrest.domain;

import com.example.personsrest.remote.GroupRemote;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons/")
@AllArgsConstructor
public class PersonController {

    PersonService personService;
    GroupRemote groupRemote;

    @GetMapping
    public List<PersonDTO> findAll() {
        return personService.findAll()
                .map(person -> this.toDTO(person))
                .collect(Collectors.toList());
    }

    @PostMapping
    public PersonDTO create(@RequestBody CreatePerson createPerson) {
        return toDTO(
                personService.createPerson(createPerson.getName(), createPerson.getAge(), createPerson.getCity()));
    }

    @GetMapping("/{id}")
    public PersonDTO get(@PathVariable("id") String id) {
        return toDTO(personService.get(id));
    }

    @PutMapping("/{id}")
    public PersonDTO update(@PathVariable("id") String id, @RequestBody UpdatePerson updatePerson) {
        return toDTO(personService.updatePerson(id, updatePerson.getName(), updatePerson.getAge(), updatePerson.getCity()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {

        personService.delete(id);
    }

    @PutMapping("/{id}/addGroup/{name}")
    public PersonDTO addPersonToGroup(@PathVariable("id") String id, @PathVariable("name")String name){
        var person = this.personService.get(id);
        var groupId = groupRemote.createGroup(name);
        person.addGroup(groupId);
        return toDTO(this.personService.save(person));
    }

    //TODO:
    //Anrop: /persons/[id]/removeGroup
    //URI - /persons/[id]/addGroup
    //Metod - GET
    //Parameter - Name, Namn på Gruppen som skall tas bort från personen.
    //Retur - Person med det ID
    @GetMapping("/{id}/removeGroup")
    public void removeGroupFromPerson(@PathVariable("id") String id, String name){

    }

    private PersonDTO toDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getCity(),
                person.getAge(),
                person.getGroups().stream().map(id -> groupRemote.getNameById(id)).collect(Collectors.toList())
        );
    }
}
