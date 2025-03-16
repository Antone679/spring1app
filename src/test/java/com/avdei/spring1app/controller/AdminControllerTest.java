package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.PersonDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.security.PersonDetails;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.validator.PersonCreateValidator;
import com.avdei.spring1app.validator.PersonUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PeopleService peopleService;
    @MockBean
    private PersonCreateValidator personValidator;
    @Autowired
    PersonUpdateValidator validator;

    @BeforeEach
    public void setUp() {
        PersonDetails personDetails = new PersonDetails(new Person(13,
                "Antone", "Antone", (short) 87, "antongru679@gmail.com",
                Role.ADMIN, null, null, null, null));
        Authentication auth = new UsernamePasswordAuthenticationToken(personDetails, "Antone",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testPerformGetUsersWithAllParamsAndGetCorrectResult() throws Exception {
        List<PersonDTO> mockPersons = List.of(new PersonDTO(10, "Jean", "000000"),
                new PersonDTO(64, "Tom", "777777"),
                new PersonDTO(4, "Zack", "12345"));
        Page<PersonDTO> mockPage = new PageImpl<>(mockPersons);

        when(peopleService.getAllUsers(eq(0),
                eq(10), eq("id"), eq("asc"))).thenReturn(mockPage);

        mockMvc.perform(get("/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("personsDTO"))
                .andExpect(content().string(containsString("Tom")))
                .andExpect(content().string(containsString("Zack")))
                .andExpect(content().string(containsString("Jean")));

    }

    @Test
    void testPerformGetUsersAndGetCorrectResultWhenNoUsers() throws Exception {
        List<PersonDTO> mockPersons = List.of();
        Page<PersonDTO> mockPage = new PageImpl<>(mockPersons);

        when(peopleService.getAllUsers(eq(0),
                eq(10), eq("id"), eq("asc"))).thenReturn(mockPage);

        mockMvc.perform(get("/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("personsDTO"))
                .andExpect(model().attribute("personsDTO", emptyIterable()));

    }

    @Test
    void testAllRequiredAttributesExist() throws Exception {
        List<PersonDTO> mockPersons = List.of();
        Page<PersonDTO> mockPage = new PageImpl<>(mockPersons);

        when(peopleService.getAllUsers(eq(0),
                eq(10), eq("id"), eq("asc"))).thenReturn(mockPage);

        mockMvc.perform(get("/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("personsDTO"))
                .andExpect(model().attributeExists("sortBy"))
                .andExpect(model().attributeExists("sortDirection"))
                .andExpect(model().attributeExists("size"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("currentPage"));

    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/admin/{id}", 10))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(peopleService).deletePerson(10);
    }

    @Test
    void testWhenRequestEditUserGetTemplate() throws Exception {
        when(peopleService.getPersonDTOById(10)).thenReturn(Optional.of(
                new PersonUpdateDTO(10, "Mike", "123456")));

        mockMvc.perform(get("/admin/edit/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit"))
                .andExpect(model().attributeExists("personDTO"));

    }

    @Test
    void testUpdateUserSuccessfullyWhenAllParamsCorrect() throws Exception {
        PersonUpdateDTO personUpdateDTO = new PersonUpdateDTO(
                10, "Tony", "77777");

        mockMvc.perform(patch("/admin/{id}", 10)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", "Tony")
                        .param("password", "77777"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(peopleService).updatePerson(eq(10),
                eq(personUpdateDTO));
    }

    @Test
    void testUpdateUserWhenParamsNotCorrect() throws Exception {
        mockMvc.perform(patch("/admin/{id}", 10)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", "To")
                        .param("password", "7"))
                .andExpect(view().name("admin/edit"))
                .andExpect(content()
                        .string(containsString("Name should be between 3 and 100 characters")))
                .andExpect(content()
                        .string(containsString("Password should be between 5 and 20 characters")))
                .andExpect(model().attributeHasFieldErrors("personDTO", "userName"))
                .andExpect(model().attributeHasFieldErrors("personDTO", "password"));
    }

}