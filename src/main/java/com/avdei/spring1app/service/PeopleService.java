package com.avdei.spring1app.service;

import com.avdei.spring1app.dto.PersonCreateDTO;
import com.avdei.spring1app.mapper.PersonMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.repository.PeopleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PersonMapper personMapper;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PersonMapper personMapper) {
        this.peopleRepository = peopleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personMapper = personMapper;
    }

    public Optional<Person> getPersonByUserName(String username) {
        return peopleRepository.findByUserName(username);
    }

    public Optional<Person> getPersonByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Optional<Person> getPersonById(int id) {
        return peopleRepository.findById(id);
    }

    public Page<Person> getAllUsers(Pageable pageable) {
        return peopleRepository.findAll(pageable);
    }

    @Transactional
    public void savePerson(PersonCreateDTO personCreateDTO) {
        Person person = personMapper.map(personCreateDTO);
        String normalizedEmail = person.getEmail().trim().toLowerCase();
        person.setEmail(normalizedEmail);
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        person.setRole(Role.USER);
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(Person person) {
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }
}
