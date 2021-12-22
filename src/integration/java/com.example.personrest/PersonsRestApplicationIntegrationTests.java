package com.example.personrest;

import com.example.personsrest.domain.Person;
import com.example.personsrest.domain.PersonRepository;
import com.example.personsrest.remote.GroupRemote;
import lombok.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
class PersonsRestApplicationIntegrationTests {
    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    GroupRemote groupRemote;

    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void test_get_persons_success() {
        // Given
        PersonDTO person1 = createPerson("Arne Anka", "Ankeborg", 100);
        person1 = addGroup(person1, "Ankeborgare");

        // When
        List<Person> persons = webTestClient.get().uri("/persons")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Person.class)
                .getResponseBody()
                .collectList()
                .block();

        // Then
        assertEquals(1, persons.size());
        assertEquals("Arne Anka", persons.get(0).getName());
        assertEquals("Ankeborgare", persons.get(0).getGroups().get(0));
    }

    private PersonDTO addGroup(PersonDTO person1, String groupName) {
        return webTestClient.get().uri("/persons/" + person1.getId() + "/addGroup?name="+ groupName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(PersonDTO.class)
                .getResponseBody()
                .blockLast();
    }

    private PersonDTO createPerson(String name, String city, int age) {
        return webTestClient.post().uri("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreatePerson(name, city, age))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(PersonDTO.class)
                .getResponseBody()
                .blockLast();
    }

    @Test
    void test_get_persons_filter_by_name_success() {
        // Given
        Person person1 = mock(Person.class);
        Page<Person> page = new PageImpl<>(List.of(person1));
        when(personRepository.findAllByNameContainingOrCityContaining(anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        // When
        webTestClient.get().uri("/persons")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.name").isEqualTo("test-webclient-repository");
    }

    @Value
    static class CreatePerson {
        String name;
        String city;
        int age;
    }

    @Value
    static class PersonDTO {
        String id;
        String name;
        String city;
        int age;
        List<String> groups;
    }

}
