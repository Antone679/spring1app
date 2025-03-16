package com.avdei.spring1app.controller;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.repository.PeopleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PeopleRepository peopleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp() {
        Person testUser = new Person();
        testUser.setUserName("Antone");
        testUser.setPassword(bCryptPasswordEncoder.encode("Antone"));
        testUser.setAge((short) 44);
        testUser.setRole(Role.USER);
        testUser.setEmail("test@example.com");
        peopleRepository.save(testUser);
    }

    @Test
    void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void testAuthenticationWithCorrectLoginAndPassword() throws Exception {

        String correctCredentials = "Antone";

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", correctCredentials)
                        .param("password", correctCredentials))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/tasks"));
    }

    @Test
    void testAuthenticationWithNoLoginAndPassword() throws Exception {

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));
    }

    @Test
    void testAuthenticationWithOnlyOneCorrectParam() throws Exception {

        String incorrectValue = "Antone10";

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", incorrectValue)
                        .param("password", "Antone"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "Antone")
                        .param("password", incorrectValue))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));
    }

    @Test
    void testRegisterSuccessWithValidParams() throws Exception {

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", "Bilbo")
                        .param("age", "48")
                        .param("password", "password")
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login"));
    }

    @Test
    void testRegisterSuccessWithNotValidParams() throws Exception {

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userName", "Bi")
                        .param("age", "-100")
                        .param("password", "12")
                        .param("email", "test_example.com"))
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeExists("personCreateDTO"))
                .andExpect(model().attributeHasFieldErrorCode("personCreateDTO", "age", "Min"))
                .andExpect(content().string(containsString("Age should be at least zero")))
                .andExpect(content().string(containsString("Password should be between 5 and 20 characters")))
                .andExpect(content().string(containsString("Name should be between 3 and 100 characters")))
                .andExpect(content().string(containsString("Invalid format of email")));
    }



}