package com.avdei.spring1app.controller;

import com.avdei.spring1app.model.Status;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.TaskService;
import com.avdei.spring1app.validator.TaskValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Getter
@Setter
@RequestMapping("/tasks")
@Tag(name = "CRUD methods for Task's entities")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskValidator taskValidator;
    private final Validator validator;

    @Autowired
    public TaskController(TaskService taskService, TaskValidator taskValidator, ApplicationEventPublisher publisher, Validator validator) {
        this.taskService = taskService;
        this.taskValidator = taskValidator;
        this.validator = validator;
    }

    @InitBinder("task")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(taskValidator);
    }

    @Operation(
            summary = "Получает весь список задач",
            description = "Получает весь список задач с пагинацией (по умолчанию 10 задач на страницу) и выводит их на странице задач"
    )
    @GetMapping
    public String getTasks(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskService.getAllTasks(pageable);
        model.addAttribute("tasks", tasks);
        log.info("Tasks returned successfully");
        return "tasks";
    }

    @Operation(summary = "Получает задачу с определенным id",
            description = "Получает задачу по айди, выводит ее на отдельной странице, посвященной этой задаче")
    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        Optional<Task> task = taskService.getTaskById(id);

        if (task.isEmpty()) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        log.info("Task returned successfully");
        return "task";
    }

    @Operation(summary = "Обрабатывает запрос на создание новой задачи",
            description = "Предоставляет форму в ответ на запрос о создании новой задачи, вкладывет в ответный запрос пустую сущность 'Task'")
    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        log.info("View form returned successfully");
        return "new";
    }

    @Operation(summary = "Обрабатывает POST запрос на создание новой задачи",
            description = "Сохраняет получаемую в POST запросе сущность 'Task' в репозитории и производит редирект на список всех задач")
    @PostMapping
    public String createTask(@ModelAttribute("task") @Valid Task task,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "new";
        }

        taskService.save(task);
        log.info("Task saved successfully");
        return "redirect:/tasks";
    }

    @Operation(summary = "Обрабатывает запрос на редактирование существующей задачи",
            description = "Получает запрос на редактирование существующей задачи, отправляет форму для редактирования с требуемой задачей")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Task task = taskService.getTaskById(id).get();
        log.info("Task has been found successfully");
        model.addAttribute("task", task);
        log.info("Show with the Task has been sent successfully");
        System.out.println(task.getDescription());
        return "edit";
    }

    @Operation(summary = "Обновляет задачу",
            description = "Обновляет задачу по id, производит редирект на страницу со всеми задачами")
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("task") Task task, BindingResult bindingResult) {

        validator.validate(task, bindingResult);

        if (bindingResult.hasErrors()){
            return "edit";
        }

        task.setId(id);
        taskService.save(task);
        log.info("Task has been updated successfully");

        return "redirect:/tasks";
    }

    @Operation(summary = "Удаляет задачу из репозитория",
            description = "Обрабатывает запрос на удаление задачи, производит редирект на список со всеми задачами")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.remove(id);
        log.info("Task has been removed successfully");
        return "redirect:/tasks";
    }

    @ModelAttribute
    private void addCommonAttributes(Model model) {
        model.addAttribute("statuses", Status.values());
    }
}
