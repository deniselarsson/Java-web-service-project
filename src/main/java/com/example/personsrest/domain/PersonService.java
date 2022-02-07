package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    public Stream<PersonEntity> findAll() {
        return Stream.of(
                new PersonEntity(UUID.randomUUID().toString(), "Arne Anka", "Ankeborg", 100));
    }
    public Person createPerson(String name, int age, String city) {
        Person person = new PersonEntity(
                UUID.randomUUID().toString(),
                name,
                age,
                city
        );
        return personRepository.save(person);
    }
    public Person get(String id) {
        return personRepository.findById(id).orElse(null);
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
