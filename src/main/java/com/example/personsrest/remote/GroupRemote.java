package com.example.personsrest.remote;


public interface GroupRemote {

    String getNameById(String groupId);

    void setNameById(String groupId, String name);
}
