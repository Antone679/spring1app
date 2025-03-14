package com.avdei.spring1app.listeners;

import com.avdei.spring1app.dto.PersonUpdateDTO;
import com.avdei.spring1app.model.Person;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PersonUpdateEvent extends ApplicationEvent {
    private final PersonUpdateDTO personUpdateDTO;
    public PersonUpdateEvent(Object source, PersonUpdateDTO personUpdateDTO) {
        super(source);
        this.personUpdateDTO = personUpdateDTO;
    }
}
