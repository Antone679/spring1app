package com.avdei.spring1app.repository;

import com.avdei.spring1app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> getAllByTaskId(Integer id);
}
