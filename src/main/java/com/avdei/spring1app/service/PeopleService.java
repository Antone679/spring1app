package com.avdei.spring1app.service;

import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.repository.PeopleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.peopleRepository = peopleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<Person> getPersonByUserName(String username){
        return peopleRepository.findByUserName(username);
    }
    public Optional<Person> getPersonByEmail(String email){
        return peopleRepository.findByEmail(email);
    }
    @Transactional
    public void savePerson(Person person){
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        person.setRole(Role.USER);
        peopleRepository.save(person);
    }
}
