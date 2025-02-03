package com.avdei.spring1app.validator;

import com.avdei.spring1app.domain.Task;
import com.avdei.spring1app.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class TaskValidator implements Validator {

    private final TaskService taskService;
    private final Validator validator;

    @Autowired
    public TaskValidator(TaskService taskService, Validator validator) {
        this.taskService = taskService;
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        Task task = (Task) target;

        if (taskService.getTaskByDescription(task.getDescription()).isPresent()) {
            errors.rejectValue("description", "", "Task with this description already exists");
        }
        errors.getAllErrors()
                .forEach(objectError -> log.error("{} - {}", objectError.getObjectName(), objectError.getDefaultMessage()));
    }
}
