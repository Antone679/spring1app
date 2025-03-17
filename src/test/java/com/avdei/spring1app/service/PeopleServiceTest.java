package com.avdei.spring1app.service;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.repository.CommentRepository;
import com.avdei.spring1app.repository.PeopleRepository;
import com.avdei.spring1app.util.CurrentUserUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class PeopleServiceTest {
    @MockBean
    private PeopleRepository peopleRepository;
    @MockBean
    private PersonMapper personMapper;
    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    BCryptPasswordEncoder encoder;
    @InjectMocks
    @Autowired
    private PeopleService peopleService;

    @Test
    void testGetPersonByUsernameWhenExists() {
        Person person = new Person(1, "Anton", "00000");
        when(peopleRepository.findByUserName("Anton")).thenReturn(Optional.of(person));

        Optional<Person> result = peopleService.getPersonByUserName("Anton");
        assertTrue(result.isPresent());
        assertEquals("Anton", result.get().getUserName());
    }

    @Test
    void testGetPersonByUsernameWhenNotExists() {
        when(peopleRepository.findByUserName("Anton")).thenReturn(Optional.empty());

        Optional<Person> result = peopleService.getPersonByUserName("Anton");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPersonByEmailWhenExists() {
        Person person = new Person(1, "Anton", "mail@mail.ru", "00000");
        when(peopleRepository.findByEmail("mail@mail.ru")).thenReturn(Optional.of(person));

        Optional<Person> result = peopleService.getPersonByEmail("mail@mail.ru");
        assertTrue(result.isPresent());
        assertEquals("Anton", result.get().getUserName());
    }

    @Test
    void testGetPersonByEmailWhenNotExists() {
        when(peopleRepository.findByUserName("Anton")).thenReturn(Optional.empty());

        Optional<Person> result = peopleService.getPersonByEmail("mail@mail.ru");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPersonByIdWhenExists() {
        Person person = new Person(1, "Anton", "mail@mail.ru", "00000");
        when(peopleRepository.findById(1)).thenReturn(Optional.of(person));

        Optional<Person> result = peopleService.getPersonById(1);
        assertTrue(result.isPresent());
        assertEquals(person.getId(), result.get().getId());
    }

    @Test
    void testGetPersonByIdWhenNotExists() {
        when(peopleRepository.findById(5)).thenReturn(Optional.empty());

        Optional<Person> result = peopleService.getPersonById(5);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPersonDTOByIdWhenExists() {
        Person person = new Person(1, "Anton", "00000");
        PersonUpdateDTO personDTO = new PersonUpdateDTO(1, "Anton", "00000");

        when(peopleRepository.findById(1)).thenReturn(Optional.of(person));
        when(personMapper.mapToUpdateDTO(person)).thenReturn(personDTO);

        Optional<PersonUpdateDTO> result = peopleService.getPersonDTOById(person.getId());

        assertTrue(result.isPresent());
        assertEquals("Anton", result.get().getUserName());
        assertEquals(1, result.get().getId());

    }

    @Test
    void testGetAllUsersReturnsCorrectNumberAndExcludesCurrentAdmin() {
        Person person1 = new Person(1, "Tony", "00000");
        Person person2 = new Person(3, "Tom", "77777");
        Page<Person> page = new PageImpl<>(List.of(person1, person2));
        when(peopleRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(personMapper.map(person1)).thenReturn(new PersonDTO(1, "Tony", "00000"));
        when(personMapper.map(person2)).thenReturn(new PersonDTO(3, "Tom", "77777"));
        try (MockedStatic<CurrentUserUtil> utilities = mockStatic(CurrentUserUtil.class)) {
            utilities.when(CurrentUserUtil::getCurrentUser).thenReturn(person1);

            Page<PersonDTO> result = peopleService.getAllUsers(1, 5, "id", "asc");

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals("Tom", result.getContent().get(0).getUserName());

        }
    }

    @Test
    void testSavePerson() {
        Person person = new Person(1, "Tony", "00000", "toNy@mail.com");
        when(personMapper.map(any(PersonCreateDTO.class))).thenReturn(person);
        when(encoder.encode("00000")).thenReturn("11111");

        peopleRepository.save(person);
        verify(peopleRepository).save(person);
    }

    @Test
    void testUpdatePerson() {
        Person person = new Person(1, "Tony", "00000", "tony@mail.com");
        PersonUpdateDTO personUpdateDTO = new PersonUpdateDTO(1, "TonyUp", "11111", "toNy@google.com");
        when(peopleRepository.findById(1)).thenReturn(Optional.of(person));
        when(encoder.encode("11111")).thenReturn("77777");

        peopleService.updatePerson(1, personUpdateDTO);

        verify(personMapper).update(eq(personUpdateDTO), eq(person));
        verify(peopleRepository).save(person);
    }

    @Test
    void testDeletePerson() {
        List<Comment> comments = List.of(new Comment(), new Comment(), new Comment());
        when(commentRepository.findByAuthorId(1)).thenReturn(comments);

        peopleService.deletePerson(1);

        verify(commentRepository).findByAuthorId(1);
        verify(commentRepository).saveAll(comments);
        verify(peopleRepository).deleteById(1);
    }

}