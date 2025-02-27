package com.avdei.spring1app.controller;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.listeners.PersonUpdateEvent;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.validator.PersonValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    final PeopleService peopleService;
    final PersonValidator personValidator;
    final ApplicationEventPublisher publisher;

    @Autowired
    public AuthenticationController(PeopleService peopleService, PersonValidator personValidator, ApplicationEventPublisher publisher) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.publisher = publisher;
    }

    @InitBinder("person")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(personValidator);
    }

    @GetMapping("/login")
    public String getLoginPage() {
        log.info("GET request authentication page");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("person", new Person());
        log.info("GET request registration page");
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("person") @Valid Person person,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        String normalizedEmail = person.getEmail().trim().toLowerCase();
        person.setEmail(normalizedEmail);
        peopleService.savePerson(person);
        ApplicationEvent event = new PersonUpdateEvent(this, person);
        publisher.publishEvent(event);
        return "redirect:/auth/login";

    }
}
