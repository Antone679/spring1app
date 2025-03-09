package com.avdei.spring1app.controller;

import com.avdei.spring1app.model.Comment;
import com.avdei.spring1app.service.CommentService;
import com.avdei.spring1app.service.TaskService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Getter
@Setter
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;

    public CommentController(CommentService commentService, TaskService taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }


}
