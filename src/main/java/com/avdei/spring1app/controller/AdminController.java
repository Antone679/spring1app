package com.avdei.spring1app.controller;

import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.validator.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;
    private final PersonMapper personMapper;
    private final PersonValidator personValidator;

    public AdminController(PeopleService peopleService, PersonMapper personMapper, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personMapper = personMapper;
        this.personValidator = personValidator;
    }


}
