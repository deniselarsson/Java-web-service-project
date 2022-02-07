package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PersonEntity implements Person {
    String id;
    String name;
    String city;
    int age;
    List<String> groups;

    public PersonEntity(String id, String name, int age, String city, List<String> groups) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.groups = groups;
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public List<String> getGroups() {
        return this.groups;
    }

    @Override
    public void addGroup(String groupId) {
        this.groups.add(groupId);
    }

    @Override
    public void removeGroup(String groupId) {
        this.groups.remove(groupId);
    }
}
