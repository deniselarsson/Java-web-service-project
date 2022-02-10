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
@RequestMapping("/api/persons/")
@AllArgsConstructor
public class PersonController {

    PersonService personService;
    GroupRemote groupRemote;
    Page page;

    @GetMapping
    public List<PersonDTO> findAll() {
        return personService.findAll()
                .map(person -> this.toDTO(person))
                .collect(Collectors.toList());
    }

    @GetMapping("/?search={name}&pagenumber=0&pagesize=10")
    public List<Person> findContains(@RequestParam("search") String search, @RequestParam("pagenumber") String pagenumber,
                                     @RequestParam("pagesize") String pagesize) {
        /*return personService.find(search, pagenumber, pagesize)
                .map(person -> this.toDTO(person))
                .collect(Collectors.toList());*/
        return null;
    }

    @GetMapping("/findAllList")
    public List<Person> findAllList() {
        return personService.findAllList();
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

    @GetMapping("/test")
    public List<Person> getAllPerson(
            @RequestParam(defaultValue = "Arne") String name,
            @RequestParam(defaultValue = "Ankeborg") String city,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        //List<Person> list = personService.findPage(name, city, pageable);
        //return personService.findAllList();
        return personService.findPage(name, city, pageable);
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
