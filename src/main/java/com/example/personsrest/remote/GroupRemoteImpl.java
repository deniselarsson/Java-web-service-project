package com.example.personsrest.remote;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupRemoteImpl implements GroupRemote{

    Map<String, String> nameToId = new HashMap<>();
    Map<String, String> idToName = new HashMap<>();

    @Override
    public String getNameById(String groupId) {
        return idToName.get(groupId);
    }

    @Override
    public String createGroup(String name) {
        var id = UUID.randomUUID().toString();
        nameToId.put(name, id);
        idToName.put(id, name);
        return id;

    }

    @Override
    public String removeGroup(String name) {
        return name;
    }
}
