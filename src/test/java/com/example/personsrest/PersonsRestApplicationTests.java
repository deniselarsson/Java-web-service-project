package com.example.personsrest;

import com.example.personrest.PersonsRestApplicationIntegrationTests;
import com.example.personsrest.domain.Person;
import com.example.personsrest.domain.PersonRepository;
import com.example.personsrest.remote.GroupRemote;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PersonsRestApplicationTests {
    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    PersonRepository personRepository;

    @MockBean
    GroupRemote groupRemote;

    PersonAPI personApi;

    @BeforeEach
    void setUp() {


    }

    @Test
    void test_get_persons_success() {
        // Given
        Person person1 = mock(Person.class);
        when(person1.getName()).thenReturn("Arne Anka");
        String groupId = UUID.randomUUID().toString();
        when(person1.getGroups()).thenReturn(List.of(groupId));
        when(personRepository.findAll()).thenReturn(List.of(person1));
        when(groupRemote.getNameById(eq(groupId))).thenReturn("Ankeborgare");

        // When
        List<PersonAPI.PersonDTO> persons = personApi.all()
                .collectList()
                .block();

        // Then
        assertEquals(1, persons.size());
        assertEquals("Arne Anka", persons.get(0).getName());
        assertEquals("Ankeborgare", persons.get(0).getGroups().get(0));
        verify(groupRemote, times(1)).getNameById(eq(groupId));
    }

    @Test
    void test_get_person_success() {
        // Given
        Person person1 = mock(Person.class);
        when(personRepository.findAll()).thenReturn(List.of(person1));

        // When
        PersonAPI.PersonDTO person = personApi.get(person1.getId())
                .block();

        // Then
        assertEquals(person1.getId(), person.getId());
    }

    @Test
    void test_create_person_success() {
        // When
        PersonAPI.PersonDTO person = personApi.createPerson("Mia", "Johannesburg", 70);

        // Then
        Person verifyPerson = personRepository.findAll();
        assertEquals(verifyPerson.getName(), person.getName());
        assertEquals(verifyPerson.getId(), person.getId());
    }

    @Test
    void test_update_person_success() {
        // Given
        Person person1 = mock(Person.class);
        when(personRepository.findById("111").thenReturn(Optional.of(person1)));

        //When
        PersonAPI.PersonDTO person = personApi.updatePerson(person1.getId(), "Sofia", null, 0);

        // Then
        Optional<Person> verifyPerson = personRepository.findById(person1.getId());
        assertEquals("Sofia", person.getName());
        assertEquals("Sofia", verifyPerson.getName());

    }

    @Test
    void test_delete_person_success() {
        // Given
        Person person1 = mock(Person.class);
        when(personRepository.findAll()).thenReturn(List.of(person1));

        // When
        personApi.deletePerson(person1.getId())
                .block();

        // Then
        List<Person> verifyPerson = personRepository.findAll().collectList().block();
        assertEquals(List.of("BBB", "CCC"), verifyPerson.stream().map(Person::getName).collect(Collectors.toList()));
    }



    // lägg till en grupp
    // ta bort en grupp



    @Test
    void test_get_persons_filter_by_name_success() {
        // Given
        Person person1 = mock(Person.class);
        Page<Person> page = new PageImpl<>(List.of(person1));
        when(personRepository.findAllByNameContainingOrCityContaining(eq("Arne"), eq("Arne"),
                any(PageRequest.class))).thenReturn(page);

        // When
        List<Person> persons = webTestClient.get().uri("/persons?search=Arne&pagenumber=0&pagesize=10")
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
    }


}
