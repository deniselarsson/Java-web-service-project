package com.example.personsrest.domain;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    public Stream<Person> findAll(String search, Integer pagenumber, Integer pagesize) {
        if(search == null || search.isEmpty()){
            return personRepository.findAll().stream();
        }
        Pageable paging = PageRequest.of(pagenumber, pagesize);
       return personRepository.findAllByNameContainingOrCityContaining(search, search, paging).stream();
    }

    public Person createPerson(String name, int age, String city) {
        Person person = new PersonEntity(
                UUID.randomUUID().toString(),
                name,
                age,
                city,
                new ArrayList<>()
        );
        return personRepository.save(person);
    }

    public Person get(String id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person updatePerson(String id, String name, int age, String city) {
        Person oldPerson = personRepository.findById(id).orElse(null);
        oldPerson.setName(name);
        oldPerson.setAge(age);
        oldPerson.setCity(city);
        return personRepository.save(oldPerson);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public void delete(String id) {
        personRepository.delete(id);
    }

    public List<Person> findAllList() {
        return personRepository.findAll();
    }

    public Page<Person> findContains(String name) {
        return personRepository.findAllByNameContainingOrCityContaining("Arne", "", Pageable.unpaged());
    }

/*    public Page<Person> findPersonWithPagination(int offset, int pageSize){
        Page<Person> personas = personRepository.findAll(PageRequest.of(offset, pageSize))
    }*/

    public Page<Person> findPage(String name, String city, Pageable pageable) {
        return personRepository.findAllByNameContainingOrCityContaining("Arne", "", Pageable.unpaged());
        //return new PageImpl();
        //Pageable paging = PageRequest.of(name, city, pageable);
    }
}
