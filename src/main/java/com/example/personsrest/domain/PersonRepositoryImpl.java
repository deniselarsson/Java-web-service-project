package com.example.personsrest.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonRepositoryImpl implements PersonRepository {
    Map<String,Person> persons = new HashMap<>();

    @Override
    public Optional<Person> findById(String id) {

        return Optional.of(persons.get(id));
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public Page<Person> findAllByNameContainingOrCityContaining(String name, String city, Pageable pageable) {
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
