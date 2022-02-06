package com.example.personsrest.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PersonService {
    public Stream<PersonEntity> findAll() {
        return Stream.of(
                new PersonEntity(UUID.randomUUID().toString(), "Arne Anka", "Ankeborg", 100));
    }
    public PersonEntity createPerson(String name, int age, String city) {
        return new PersonEntity(
                UUID.randomUUID().toString(),
                name,
                age,
                city
        );
    }
    public PersonEntity get(String id) {
        return new PersonEntity(
                id,
                "Arne Anka",
                100,
                "Ankeborg"
        );
    }
    public PersonEntity updatePerson(String id,String name, int age, String city) {
        return new PersonEntity(
                id,
                name,
                age,
                city
        );
    }
    public void delete(String id) {
    }
}
