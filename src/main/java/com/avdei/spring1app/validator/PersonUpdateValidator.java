package com.avdei.spring1app.validator;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@Slf4j
public class PersonUpdateValidator implements Validator {

    private final Validator validator;
    private final PeopleService peopleService;
    private final MessageSource messageSource;

    @Autowired
    public PersonUpdateValidator(Validator validator, PeopleService peopleService, MessageSource messageSource) {
        this.validator = validator;
        this.peopleService = peopleService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        if (target instanceof PersonUpdateDTO) {
            PersonUpdateDTO personUpdateDTO = (PersonUpdateDTO) target;

            Person currentPerson = peopleService.getPersonById(personUpdateDTO.getId()).orElse(null);

            if (currentPerson != null) {
                boolean userNameExists = peopleService.getPersonByUserName(personUpdateDTO.getUserName()).isPresent();
                boolean isUpdatingSameUser = currentPerson.getUserName().equals(personUpdateDTO.getUserName());

                if (userNameExists && !isUpdatingSameUser) {
                    errors.rejectValue("userName", "",
                            messageSource.getMessage("reg.username.exists",
                                    null, LocaleContextHolder.getLocale()));
                }

                boolean emailExists = peopleService.getPersonByEmail(personUpdateDTO.getEmail().trim().toLowerCase()).isPresent();
                boolean isUpdatingSameEmail = currentPerson.getEmail().equals(personUpdateDTO.getEmail());

                if (emailExists && !isUpdatingSameEmail) {
                    errors.rejectValue("email", "",
                            messageSource.getMessage("reg.email.exists",
                                    null, LocaleContextHolder.getLocale()));
                }
            }
        }
    }
}
