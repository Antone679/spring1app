package com.avdei.spring1app.repository;

import com.avdei.spring1app.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUserName(String userName);
    Optional<Person> findByEmail(String email);
    @EntityGraph(attributePaths = "tasks")
    Page<Person> findAll(Pageable pageable);
}
