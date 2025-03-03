package com.avdei.spring1app.controller;

import com.avdei.spring1app.dto.TaskCreateDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.mapper.TaskMapper;
import com.avdei.spring1app.model.Person;
import com.avdei.spring1app.model.Role;
import com.avdei.spring1app.model.Status;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.service.TaskService;
import com.avdei.spring1app.util.CurrentUserUtil;
import com.avdei.spring1app.util.DurationConverter;
import com.avdei.spring1app.validator.TaskValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Getter
@Setter
@RequestMapping("/tasks")
@Tag(name = "CRUD methods for Task's entities")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskValidator taskValidator;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskValidator taskValidator, ApplicationEventPublisher publisher, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskValidator = taskValidator;
        this.taskMapper = taskMapper;
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
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam(defaultValue = "id") String sortBy,
                           @RequestParam(defaultValue = "asc") String sortDirection,
                           @RequestParam(defaultValue = "false") boolean showMyTasks,
                           Model model) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks;

        if (showMyTasks) {
            Integer currentUserId = CurrentUserUtil.getCurrentUser().getId();
            tasks = taskService.getMyTasks(currentUserId, pageable);
        } else {
            tasks = taskService.getAllTasks(pageable);
        }

        List<TaskDTO> tasksDTOList = tasks.getContent()
                .stream()
                .map(taskMapper::map)
                .toList();

        Page<TaskDTO> tasksDTO = new PageImpl<>(tasksDTOList, pageable, tasks.getTotalElements());

        model.addAttribute("tasksDTO", tasksDTO);
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
        TaskDTO taskDTO = taskMapper.map(task.get());
        updateDurationToBeShown(taskDTO);

        model.addAttribute("taskDTO", taskDTO);
        log.info("Task returned successfully");
        return "task";
    }

    @Operation(summary = "Обрабатывает запрос на создание новой задачи",
            description = "Предоставляет форму в ответ на запрос о создании новой задачи, вкладывет в ответный запрос пустую сущность 'TaskCreateDTO'")
    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("taskCreateDTO", new TaskCreateDTO());
        log.info("View form returned successfully");
        return "new";
    }

    @Operation(summary = "Обрабатывает POST запрос на создание новой задачи",
            description = "Сохраняет получаемую в POST запросе сущность 'TaskCreateDTO' в репозитории и производит редирект на список всех задач")
    @PostMapping
    public String createTask(@ModelAttribute("taskCreateDTO") @Valid TaskCreateDTO taskCreateDTO,
                             BindingResult bindingResult) {

        taskValidator.validate(taskCreateDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }
        Task task = taskMapper.map(taskCreateDTO);

        taskService.save(task);
        log.info("Task saved successfully");
        return "redirect:/tasks";
    }

    @Operation(summary = "Обрабатывает запрос на редактирование существующей задачи",
            description = "Получает запрос на редактирование существующей задачи, отправляет форму для редактирования с требуемой задачей")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Task task = taskService.getTaskById(id).get();
        TaskUpdateDTO taskDTO = taskMapper.mapToUpdateDTO(task);
        log.info("Task has been found successfully");
        model.addAttribute("taskDTO", taskDTO);
        log.info("Template with the Task has been sent successfully");
        System.out.println(task.getDescription());
        return "edit";
    }

    @Operation(summary = "Обновляет задачу",
            description = "Обновляет задачу по id, производит редирект на страницу со всеми задачами")
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("taskDTO") TaskUpdateDTO taskUpdateDTO, BindingResult bindingResult) {
        taskValidator.validate(taskUpdateDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }
        Task task = taskService.getTaskById(id).get();
        taskMapper.update(taskUpdateDTO, task);

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

    @ModelAttribute
    private void checkAdminRights(Model model) {
        Person currentUser = CurrentUserUtil.getCurrentUser();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        model.addAttribute("admin", isAdmin);
    }

    private void updateDurationToBeShown(TaskDTO taskDTO) {
        String durationAsString;
        if (taskDTO.getStatus() == Status.IN_PROGRESS) {
            Instant now = Instant.now();
            Instant updatedAt = taskDTO.getUpdatedAt().toInstant();
            long durationSinceLastUpdate = Duration.between(updatedAt, now).toMillis();
            durationAsString = DurationConverter
                    .formatDuration(taskDTO.getDuration() + durationSinceLastUpdate);
            taskDTO.setCurrentShow(durationAsString);
        } else {
            durationAsString = DurationConverter.formatDuration(taskDTO.getDuration());
            if (durationAsString.isBlank())
                durationAsString = "0";
            taskDTO.setCurrentShow(durationAsString);
        }
    }
}
