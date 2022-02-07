package com.example.personsrest.domain;

import lombok.Value;

@Value
public class CreatePerson {
    String name;
    int age;
    String city;
}
