package com.avdei.spring1app.repository;

import com.avdei.spring1app.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByDescription(String description);
    @EntityGraph(attributePaths = "author")
    Page<Task> findByAuthorId(Integer authorId, Pageable pageable);
    @EntityGraph(attributePaths = "author")
    Page<Task> findAll(Pageable pageable);

}
