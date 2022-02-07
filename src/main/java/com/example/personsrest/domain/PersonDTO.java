package com.example.personsrest.domain;

import lombok.Value;

import java.util.List;

@Value
public class PersonDTO {
        String id;
        String name;
        String city;
        int age;
        List<String> groups;
}
