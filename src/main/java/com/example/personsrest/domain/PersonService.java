package com.example.personsrest.domain;

import com.example.personsrest.remote.GroupRemote;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;
    GroupRemote groupRemote;

    public Stream<Person> findAll(String search, Integer pagenumber, Integer pagesize) {
        if (search == null || search.isEmpty()) {
            return personRepository.findAll().stream();
        }
        Pageable paging = PageRequest.of(pagenumber, pagesize);
        return personRepository.findAllByNameContainingOrCityContaining(search, search, paging).stream();
    }

    public Person createPerson(String name, int age, String city) {
        Person person = new PersonEntity(
                UUID.randomUUID().toString(),
                name,
                age,
                city,
                new ArrayList<>()
        );
        return personRepository.save(person);
    }

    public Person get(String id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person updatePerson(String id, String name, int age, String city) {
        Person person = personRepository.findById(id).orElse(null);
        person.setName(name);
        person.setAge(age);
        person.setCity(city);
        return personRepository.save(person);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public void delete(String id) {
        personRepository.delete(id);
    }

    public Person addPersonToGroup(String id,String name){
        var person = get(id);
        var groupId = groupRemote.createGroup(name);
        person.addGroup(groupId);
        return save(person);
    }

    public Person removeGroup(String id, String name){
        var person = get(id);
        var groups = person.getGroups().toArray(new String[0]);
        for (var groupId : groups) {
            if (groupRemote.getNameById(groupId).equals(name)) {
                groupRemote.removeGroup(name);
                person.removeGroup(groupId);
            }
        }
        return save(person);
    }
}
