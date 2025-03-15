package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.listeners.PersonCreationEvent;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.validator.PersonCreateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CRUD methods for Person's entities")
@Slf4j
public class AuthenticationController {
    final PeopleService peopleService;
    final PersonCreateValidator personValidator;
    final ApplicationEventPublisher publisher;

    @Autowired
    public AuthenticationController(PeopleService peopleService, PersonCreateValidator personValidator, ApplicationEventPublisher publisher) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.publisher = publisher;
    }

    @InitBinder("personCreateDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(personValidator);
    }

    @Operation(
            summary = "Возвращается страницу login"
    )
    @GetMapping("/login")
    public String getLoginPage() {
        log.info("GET request authentication page");
        return "auth/login";
    }

    @Operation(
            summary = "Возвращает страницу регистрации"
    )
    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("personCreateDTO", new PersonCreateDTO());
        log.info("GET request registration page");
        return "auth/registration";
    }
    //
    @Operation(
            summary = "Проводит процесс регистрации"
    )
    @PostMapping("/registration")
    public String register(@ModelAttribute("personCreateDTO") @Valid PersonCreateDTO personCreateDTO,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        peopleService.savePerson(personCreateDTO);
        ApplicationEvent event = new PersonCreationEvent(this, personCreateDTO);
        publisher.publishEvent(event);
        return "redirect:/auth/login";

    }
}
