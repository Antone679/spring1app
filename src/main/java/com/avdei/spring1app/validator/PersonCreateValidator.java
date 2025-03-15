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
public class PersonCreateValidator implements Validator {

    private final Validator validator;
    private final PeopleService peopleService;
    private final PersonMapper personMapper;

    @Autowired
    public PersonCreateValidator(Validator validator, PeopleService peopleService, PersonMapper personMapper) {
        this.validator = validator;
        this.peopleService = peopleService;
        this.personMapper = personMapper;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return PersonCreateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        if (target instanceof PersonCreateDTO) {
            PersonCreateDTO personCreateDTO = (PersonCreateDTO) target;
            Person person = personMapper.map(personCreateDTO);

            boolean userNameExists = peopleService.getPersonByUserName(person.getUserName()).isPresent();

            if (userNameExists) {
                errors.rejectValue("userName", "", "Person already exists with this username");
                log.warn("Validation error: Person already exists with username {}", person.getUserName());
            }

            boolean emailExists = peopleService.getPersonByEmail(person.getEmail().trim().toLowerCase()).isPresent();

            if (emailExists) {
                errors.rejectValue("email", "", "Email already exists");
                log.warn("Validation error: Person already exists with email {}", person.getEmail());
            }
        }
    }
}
