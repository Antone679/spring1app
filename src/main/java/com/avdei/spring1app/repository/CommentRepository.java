package com.avdei.spring1app.repository;

import com.avdei.spring1app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.task.id = ?1")
    List<Comment> getAllByTaskId(Integer id);
    @Query("SELECT c FROM Comment c WHERE c.author.id = ?1")
    List<Comment> findByAuthorId(int id);
}
