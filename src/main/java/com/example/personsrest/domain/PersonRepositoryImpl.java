package com.example.personsrest.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

public class PersonRepositoryImpl implements PersonRepository {
    Map<String, Person> persons = new HashMap<>();
    List<Person> personsList = new ArrayList<>();

    @Override
    public Optional<Person> findById(String id) {

        return Optional.of(persons.get(id));
    }

    @Override
    public List<Person> findAll() {
        persons.forEach((k, v) -> {
            personsList.add(v);
        });

        return personsList;
    }

    @Override
    public Page<Person> findAllByNameContainingOrCityContaining(String name, String city, Pageable pageable) {
        persons.forEach((k, v) -> {
            personsList.add(v);
        });

        if(personsList.contains(name)) {
            Page pageList = (Page) personsList.stream().sorted().collect(Collectors.toList());
            return pageList;
        }
        return null;
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public Person save(Person person) {
        persons.put(person.getId(), person);
        return person;
    }

    @Override
    public void delete(String id) {

    }
}
