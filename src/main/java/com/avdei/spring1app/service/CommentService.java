package com.avdei.spring1app.service;

import com.avdei.spring1app.dto.CommentDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.repository.CommentRepository;
import com.avdei.spring1app.repository.TaskRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PeopleService peopleService;
    private final TaskServiceImpl taskService;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PeopleService peopleService, TaskServiceImpl taskService, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.peopleService = peopleService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    public List<Comment> getCommentsForTask(Integer id) {
        return commentRepository.getAllByTaskId(id);
    }

    @Transactional
    public void createComment(int id, CommentDTO commentDTO, Principal principal) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getCommentText());
        Optional<Person> author = peopleService.getPersonByUserName(principal.getName());
        Optional<TaskDTO> task = taskService.getTaskById(id);

        if (author.isPresent() && task.isPresent()) {
            comment.setAuthor(author.get());

            Task taskToBind = taskRepository.findById(task.get().getId()).get();
            comment.setTask(taskToBind);
            commentRepository.save(comment);
        }

    }

    @Transactional
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
