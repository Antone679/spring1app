package com.avdei.spring1app.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonEventListener {

    @EventListener
    public void handleUserCreationEvent(PersonCreationEvent event) {
        log.warn("NAME OF CREATED PERSON: {}", event.getPerson().getUserName());
    }
    @EventListener
    public void handleUserUpdateEvent(PersonUpdateEvent event) {
        log.warn("NAME OF UPDATED PERSON: {}", event.getPerson().getUserName());
    }
}
