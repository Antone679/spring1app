package com.avdei.spring1app.validator;

import com.avdei.spring1app.dto.TaskCreateDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.mapper.TaskMapper;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@Slf4j
public class TaskValidator implements Validator {

    private final TaskService taskService;
    private final Validator validator;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskValidator(TaskService taskService, Validator validator, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.validator = validator;
        this.taskMapper = taskMapper;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return Task.class.equals(clazz) ||
                TaskCreateDTO.class.equals(clazz) ||
                TaskUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);
        Task task = null;

        if (target instanceof TaskCreateDTO) {
            task = taskMapper.map((TaskCreateDTO) target);
        } else if (target instanceof TaskUpdateDTO) {
            TaskUpdateDTO updateDTO = (TaskUpdateDTO) target;
            task = taskService.getTaskById(updateDTO.getId()).orElse(null);
        } else {
            task = (Task) target;
        }

        if (task != null && task.getDescription() != null) {
            if (target instanceof TaskCreateDTO) {
                if (taskService.getTaskByDescription(task.getDescription()).isPresent()) {
                    errors.rejectValue("description", "", "Task with this description already exists");
                }
            } else if (target instanceof TaskUpdateDTO) {
                TaskUpdateDTO updateDTO = (TaskUpdateDTO) target;
                String newDescription = updateDTO.getDescription();
                Integer taskId = updateDTO.getId();

                Optional<Task> existingTaskWithSameDescription = taskService.getTaskByDescription(newDescription);

                if (existingTaskWithSameDescription.isPresent() &&
                        !existingTaskWithSameDescription.get().getId().equals(taskId)) {
                    errors.rejectValue("description", "", "Task with this description already exists");
                }
            }
        }

    }
}
