package com.avdei.spring1app.validator;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class PersonValidator implements Validator {

    private final Validator validator;
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(Validator validator, PeopleService peopleService) {
        this.validator = validator;
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        Person person = (Person) target;

        if (peopleService.getPersonByUserName(person.getUserName()).isPresent()) {
            errors.rejectValue("userName", "", "Person already exists");
            log.warn("Validation error: Person already exists with userName {}", person.getUserName());
        }
        if (peopleService.getPersonByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Email already exists");
            log.warn("Validation error: Person already exists with email {}", person.getEmail());
        }
    }
}
