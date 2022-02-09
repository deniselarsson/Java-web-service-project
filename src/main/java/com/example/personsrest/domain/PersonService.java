package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    public Stream<PersonEntity> findAll() {
        return Stream.of(
                new PersonEntity(UUID.randomUUID().toString(), "Arne Anka", "Ankeborg", 100, List.of()));
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
        Person oldPerson = personRepository.findById(id).orElse(null);
        oldPerson.setName(name);
        oldPerson.setAge(age);
        oldPerson.setCity(city);
        return personRepository.save(oldPerson);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public void delete(String id) {
        personRepository.delete(id);
    }
}
