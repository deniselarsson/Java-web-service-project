package com.example.personsrest.domain;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class PersonTest implements Person {

    String id = UUID.randomUUID().toString();
    String name;
    int age;
    String city;
    //List<String> group;
    String groupId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {

    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public List<String> getGroups() {
        return null;
    }

    @Override
    public void addGroup(String groupId) {

    }

    @Override
    public void removeGroup(String groupId) {

    }
}
