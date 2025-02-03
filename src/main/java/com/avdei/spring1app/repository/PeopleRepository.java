package com.avdei.spring1app.repository;

import com.avdei.spring1app.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUserName(String userName);
    Optional<Person> findByEmail(String email);
}
