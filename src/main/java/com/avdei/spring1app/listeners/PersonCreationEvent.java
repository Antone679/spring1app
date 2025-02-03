package com.avdei.spring1app.listeners;

import com.avdei.spring1app.domain.Person;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PersonCreationEvent extends ApplicationEvent {
    private final Person person;

    public PersonCreationEvent(Object source, Person person) {
        super(source);
        this.person = person;

    }
}
