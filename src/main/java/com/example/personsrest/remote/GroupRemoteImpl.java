package com.example.personsrest.remote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Objects;

public class GroupRemoteImpl implements GroupRemote {

    private static final String BASE_URL = "api/groups/";
    private static final String WEB_CLIENT_URL = "https://groups.edu.sensera.se/";
    private static final String KEY_CLOAK_TOKEN_URL = "https://iam.sensera.se/";

    private final WebClient webClient;
    private final KeyCloakToken keyCloakToken;
    final int TIMEOUT = 30;

    public GroupRemoteImpl() {
        this.webClient = WebClient.create(WEB_CLIENT_URL);
        this.keyCloakToken = KeyCloakToken.acquire(
                KEY_CLOAK_TOKEN_URL,
                "test",
                "group-api",
                "user",
                "djnJnPf7VCQvp3Fc"
        ).block(Duration.ofSeconds(TIMEOUT));
    }

    @Override
    public String getNameById(String groupId) {
        return Objects.requireNonNull(webClient.get().uri(BASE_URL + groupId)
                .header(
                        "Authorization",
                        "Bearer " + keyCloakToken.getAccessToken())
                .retrieve()
                .bodyToMono(GroupDTO.class)
                .block()).getName();
    }

    @Override
    public String createGroup(String name) {
        return Objects.requireNonNull(webClient.post().uri(BASE_URL)
                .header(
                        "Authorization",
                        "Bearer " + keyCloakToken.getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(new CreateGroupDTO(name))
                .retrieve()
                .bodyToMono(GroupDTO.class)
                .block()).getId();
    }

    @Override
    public String removeGroup(String id) {
        return webClient.delete().uri(BASE_URL + id)
                .header(
                        "Authorization",
                        "Bearer " + keyCloakToken.getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GroupDTO.class)
                .toString();
    }

    @Value
    static class GroupDTO {
        String id;
        String name;

        @JsonCreator
        public GroupDTO(@JsonProperty("id") String id,
                        @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Value
    static class CreateGroupDTO {
        String name;
    }
}
