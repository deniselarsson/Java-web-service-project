package com.example.personsrest.domain;

import com.example.personsrest.remote.GroupRemote;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
@AllArgsConstructor
public class PersonController {

    PersonService personService;
    GroupRemote groupRemote;

    @GetMapping
    public List<PersonDTO> findAll(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "pagenumber", required = false) Integer pagenumber,
                                   @RequestParam(value = "pagesize", required = false) Integer pagesize) {
        return personService.findAll(search, pagenumber, pagesize)
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
    public PersonDTO addPersonToGroup(@PathVariable("id") String id, @PathVariable("name") String name) {
        var person = personService.get(id);
        var groupId = groupRemote.createGroup(name);
        person.addGroup(groupId);
        return toDTO(
                personService.save(person));
    }

    @DeleteMapping("/{id}/removeGroup/{name}")
    public PersonDTO removeGroup(@PathVariable("id") String id, @PathVariable("name") String name) {
        var person = personService.get(id);
        var groups = person.getGroups().toArray(new String[0]);
        for (var groupId : groups) {
            if (groupRemote.getNameById(groupId).equals(name)) {
                groupRemote.removeGroup(name);
                person.removeGroup(groupId);
            }
        }
        return toDTO(personService.save(person));
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
