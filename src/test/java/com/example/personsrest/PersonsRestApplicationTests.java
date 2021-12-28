package com.example.personsrest;

import com.example.personsrest.domain.Person;
import com.example.personsrest.domain.PersonRepository;
import com.example.personsrest.remote.GroupRemote;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        personApi = new PersonAPI(webTestClient);

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
        Person verifyPerson = personRepository.findById(person.getId()).get();
        assertEquals( "Mia", verifyPerson.getName());
        assertEquals( "Mia", person.getName());
        assertEquals(verifyPerson.getCity(), person.getCity());
        assertEquals(verifyPerson.getAge(), person.getAge());
    }

    @Test
    void test_update_person_success() {
        // Given
        Person person1 = mock(Person.class);
        when(personRepository.findById(person1.getId())).thenReturn(Optional.of(person1));

        //When
        PersonAPI.PersonDTO personUpdated = personApi.updatePerson(person1.getId(), "Sofia", "Stockholm", 8);

        // Then
        Person verifyPerson = personRepository.findById(person1.getId()).get();
        assertEquals("Sofia", personUpdated.getName());
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
        assertFalse(personRepository.findById(person1.getId()).isPresent());
    }

    @Test
    void test_add_group_to_person_success() {
        // Given
        String groupId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        Person person = mock(Person.class);
        Person person2 = mock(Person.class);
        when(person2.getGroups()).thenReturn(List.of(groupId));
        when(personRepository.findById(eq(personId))).thenReturn(Optional.of(person));
        when(groupRemote.createGroup(eq("Ankeborgare"))).thenReturn(groupId);
        when(personRepository.save(eq(person))).thenReturn(person2);

        // When
        PersonAPI.PersonDTO personWithAddedGroup = personApi.addGroup(personId, "Ankeborgare");

        // Then
        assertEquals("Ankeborgare", personWithAddedGroup.getGroups().get(0));
        verify(groupRemote, times(1)).createGroup(eq("Ankeborgare"));
        verify(person, times(1)).addGroup(eq(groupId));
    }

    @Test
    void tets_remove_group_from_person_success() {
        // Given
        String groupId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        Person person = mock(Person.class);
        when(person.getGroups()).thenReturn(List.of(groupId));
        Person person2 = mock(Person.class);
        when(person2.getGroups()).thenReturn(List.of());
        when(personRepository.findById(eq(personId))).thenReturn(Optional.of(person));
        when(personRepository.save(eq(person))).thenReturn(person2);

        // When
        PersonAPI.PersonDTO personWithRemovedGroup = personApi.removeGroup(personId, groupId);

        // Then
        assertEquals(groupId, personWithRemovedGroup.getGroups().get(0));
        verify(groupRemote, times(0)).removeGroup(eq(groupId));
        verify(person, times(1)).removeGroup(eq(groupId));
        verify(personRepository, times(1)).save(eq(person));
    }

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
