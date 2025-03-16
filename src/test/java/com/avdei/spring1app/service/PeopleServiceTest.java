package com.avdei.spring1app.service;

import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PeopleServiceTest {
    @MockBean
    private PeopleRepository peopleRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private PersonMapper personMapper;
    @InjectMocks
    @Autowired
    private PeopleService peopleService;
    @Test
    void testGetPersonByUsernameWhenExists(){
        Person person = new Person(1, "Anton", "00000");
        when(peopleRepository.findByUserName("Anton")).thenReturn(Optional.of(person));

        Optional<Person> result = peopleService.getPersonByUserName("Anton");
        assertTrue(result.isPresent());
        assertEquals("Anton", result.get().getUserName());
    }
    @Test
    void testGetPersonByUsernameWhenNotExists(){
        when(peopleRepository.findByUserName("Anton")).thenReturn(Optional.empty());

        Optional<Person> result = peopleService.getPersonByUserName("Anton");
        assertTrue(result.isEmpty());
    }

}