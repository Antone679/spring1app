package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.security.PersonDetails;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.validator.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeopleService peopleService;

    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private PersonValidator personValidator;

    @BeforeEach
    public void setUp(){
        PersonDetails personDetails = new PersonDetails(new Person(13, "Antone", "Antone", (short) 87, "antongru679@gmail.com",
                Role.ADMIN, null, null, null, null));
        Authentication auth = new UsernamePasswordAuthenticationToken(personDetails, "Antone",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    @Test
    public void performGetUsers_withAllParams_andGetCorrectResult() throws Exception {
        List<Person> mockPersons = List.of(new Person(), new Person(), new Person());
        Page<Person> mockPage = new PageImpl<>(mockPersons);

        when(peopleService.getAllUsers(any(Pageable.class))).thenReturn(mockPage);
        when(personMapper.map(any(Person.class))).thenReturn(new PersonDTO(100, "Stays", "12345"));

        mockMvc.perform(get("/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("personsDTO"));
    }
}