package com.example.personrest;

import com.example.personsrest.PersonAPI;
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
public class PersonsRestApplicationIntegrationTests {
    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    GroupRemote groupRemote;

    PersonAPIInegration personAPIInegration;

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
        PersonAPIInegration.PersonDTO person1 = personAPIInegration.createPerson("Arne Anka", "Ankeborg", 100);
        person1 = personAPIInegration.addGroup(person1, "Ankeborgare");

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

}
