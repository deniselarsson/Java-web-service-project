package com.example.personsrest.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository {
    List<Person> findAll();
    Page<Person> findAllByNameContainingOrCityContaining(String name, String city, Pageable pageable);

    void deleteAll();
}
