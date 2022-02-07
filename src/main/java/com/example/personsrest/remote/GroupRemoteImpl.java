package com.example.personsrest.remote;

public class GroupRemoteImpl implements GroupRemote{


    @Override
    public String getNameById(String groupId) {
        return groupId;
    }

    @Override
    public String createGroup(String name) {
        return name;
    }

    @Override
    public String removeGroup(String name) {
        return name;
    }
}
