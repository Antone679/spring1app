package com.avdei.spring1app.controller;

import com.avdei.spring1app.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PeopleRepository peopleRepository;

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }
    @Test
    public void testAuthenticationWithCorrectLoginAndPassword() throws Exception {

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "Antone")
                        .param("password", "Antone"))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/tasks"));
    }

    @Test
    public void testAuthenticationWithNoLoginAndPassword() throws Exception {

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));
    }
    @Test
    public void testAuthenticationWithOnlyOneCorrectParam() throws Exception {

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "Antone10")
                        .param("password", "Antone"))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));

        mockMvc.perform(post("/process_login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "Antone")
                        .param("password", "Antone10"))

                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/auth/login?error"));
    }
}