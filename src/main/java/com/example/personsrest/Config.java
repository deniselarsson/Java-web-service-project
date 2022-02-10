package com.example.personsrest;

import com.example.personsrest.domain.PageImpl;
import com.example.personsrest.domain.PersonRepository;
import com.example.personsrest.domain.PersonRepositoryImpl;
import com.example.personsrest.remote.GroupRemote;
import com.example.personsrest.remote.GroupRemoteImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

@Configuration
public class Config {
    @Bean
    public GroupRemote groupRemote() {
        return new GroupRemoteImpl();
    }

    @Bean
    public PersonRepository personRepository() {
        return new PersonRepositoryImpl();
    }

    @Bean
    public Page page(){ return new PageImpl();
    }
}
