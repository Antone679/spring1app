package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.CommentDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.mapper.CommentMapper;
import com.avdei.spring1app.mapper.TaskMapper;
import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.CommentService;
import com.avdei.spring1app.service.PeopleService;
import com.avdei.spring1app.service.TaskService;
import com.avdei.spring1app.util.CurrentUserUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@Getter
@Setter
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final PeopleService peopleService;
    private final TaskMapper taskMapper;

    public CommentController(CommentService commentService, TaskService taskService, CommentMapper commentMapper, PeopleService peopleService, TaskMapper taskMapper) {
        this.commentService = commentService;
        this.taskService = taskService;
        this.commentMapper = commentMapper;
        this.peopleService = peopleService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/tasks/{id}/comments")
    public String createComment(@PathVariable int id, @ModelAttribute @Valid CommentDTO commentDTO,
                                BindingResult bindingResult, Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            TaskDTO taskDTO = taskMapper.map(taskService.getTaskById(id).get());
            model.addAttribute("taskDTO", taskDTO);
            model.addAttribute("commentDTO", commentDTO);
            return "task";
        }

        Comment comment = new Comment();
        comment.setText(commentDTO.getCommentText());
        Optional<Person> author = peopleService.getPersonByUserName(principal.getName());
        Optional<Task> task = taskService.getTaskById(id);

        if (author.isPresent() && task.isPresent()) {
            comment.setAuthor(author.get());
            comment.setTask(task.get());
            commentService.createComment(comment);
        }

        return "redirect:/tasks/" + id;
    }

    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public String deleteComment(@PathVariable int taskId, @PathVariable int commentId){
        commentService.deleteComment(commentId);

        return "redirect:/tasks/" + taskId;
    }

    @ModelAttribute
    private void checkAdminRights(Model model) {
        Person currentUser = CurrentUserUtil.getCurrentUser();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        Integer isMyTask = currentUser.getId();
        model.addAttribute("admin", isAdmin);
        model.addAttribute("isMyTask", isMyTask);
    }
}
