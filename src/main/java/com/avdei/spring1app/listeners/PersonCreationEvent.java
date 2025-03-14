package com.avdei.spring1app.listeners;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.model.Person;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PersonCreationEvent extends ApplicationEvent {
    private final PersonCreateDTO personCreateDTO;

    public PersonCreationEvent(Object source, PersonCreateDTO personCreateDTO) {
        super(source);
        this.personCreateDTO = personCreateDTO;

    }
}
