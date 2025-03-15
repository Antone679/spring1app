package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.CommentDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.service.CommentService;
import com.avdei.spring1app.service.TaskServiceImpl;
import com.avdei.spring1app.util.CurrentUserUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Getter
@Setter
public class CommentController {

    private final CommentService commentService;
    private final TaskServiceImpl taskService;

    public CommentController(CommentService commentService, TaskServiceImpl taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }

    @PostMapping("/tasks/{id}/comments")
    public String createComment(@PathVariable int id, @ModelAttribute @Valid CommentDTO commentDTO,
                                BindingResult bindingResult, Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            TaskDTO taskDTO = taskService.getTaskById(id).get();
            model.addAttribute("taskDTO", taskDTO);
            model.addAttribute("commentDTO", commentDTO);
            return "task";
        }
        commentService.createComment(id, commentDTO, principal);

        return "redirect:/tasks/" + id;
    }

    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public String deleteComment(@PathVariable int taskId, @PathVariable int commentId){
        commentService.deleteComment(commentId);

        return "redirect:/tasks/" + taskId;
    }

    @ModelAttribute
    private void checkAdminAndEditorRights(Model model) {
        Person currentUser = CurrentUserUtil.getCurrentUser();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        Integer isMyTask = currentUser.getId();
        model.addAttribute("admin", isAdmin);
        model.addAttribute("isMyTask", isMyTask);
    }
}
