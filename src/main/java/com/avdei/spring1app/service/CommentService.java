package com.avdei.spring1app.service;

import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.repository.CommentRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
@Setter
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsForTask(Integer id) {
        return commentRepository.getAllByTaskId(id);
    }

    @Transactional
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
