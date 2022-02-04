package com.example.personsrest.domain;

import lombok.Value;

@Value
public class PersonDTO {
        String id;
        String name;
        String city;
        int age;
}
