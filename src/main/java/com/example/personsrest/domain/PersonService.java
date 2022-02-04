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
}
