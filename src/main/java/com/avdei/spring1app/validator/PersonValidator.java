package com.avdei.spring1app.validator;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
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
    private final PersonMapper personMapper;

    @Autowired
    public PersonValidator(Validator validator, PeopleService peopleService, PersonMapper personMapper) {
        this.validator = validator;
        this.peopleService = peopleService;
        this.personMapper = personMapper;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return PersonCreateDTO.class.equals(clazz) || PersonUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);
        Person person;
        PersonUpdateDTO personUpdateDTO = null;

        if (target instanceof PersonCreateDTO) {
            PersonCreateDTO personCreateDTO = (PersonCreateDTO) target;
            person = personMapper.map(personCreateDTO);
        } else {
            personUpdateDTO = (PersonUpdateDTO) target;
            person = peopleService.getPersonById(personUpdateDTO.getId()).orElse(null);
        }

        if (person != null) {
            if (peopleService.getPersonByUserName(person.getUserName()).isPresent() &&
                    (personUpdateDTO == null || !person.getId().equals(personUpdateDTO.getId()) ||
                            !person.getUserName().equals(personUpdateDTO.getUserName()))) {
                errors.rejectValue("userName", "", "Person already exists");
                log.warn("Validation error: Person already exists with userName {}", person.getUserName());
            }

            if (peopleService.getPersonByEmail(person.getEmail().trim().toLowerCase()).isPresent() &&
                    (personUpdateDTO == null || !person.getId().equals(personUpdateDTO.getId()) ||
                            !person.getEmail().equals(personUpdateDTO.getEmail()))) {
                errors.rejectValue("email", "", "Email already exists");
                log.warn("Validation error: Person already exists with email {}", person.getEmail());
            }
        }
    }
}
