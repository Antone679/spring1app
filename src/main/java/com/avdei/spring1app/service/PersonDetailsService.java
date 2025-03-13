package com.avdei.spring1app.service;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.repository.PeopleRepository;
import com.avdei.spring1app.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUserName(username);

        if (person.isEmpty())
            throw new UsernameNotFoundException("No user found");

        return new PersonDetails(person.get());
    }
}
