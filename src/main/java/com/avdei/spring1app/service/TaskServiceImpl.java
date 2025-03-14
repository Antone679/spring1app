package com.avdei.spring1app.service;

import com.avdei.spring1app.dto.CommentDTO;
import com.avdei.spring1app.dto.TaskCreateDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.mapper.CommentMapper;
import com.avdei.spring1app.mapper.TaskMapper;
import com.avdei.spring1app.model.Status;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.repository.TaskRepository;
import com.avdei.spring1app.util.CurrentUserUtil;
import com.avdei.spring1app.util.DurationConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
@Slf4j
public class TaskServiceImpl {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, CommentMapper commentMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.commentMapper = commentMapper;
    }

    public Page<TaskDTO> getAllTasks(int page, Integer size, String sortBy,
                                     String sortDirection, boolean showMyTasks) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks;

        if (showMyTasks) {
            Integer currentUserId = CurrentUserUtil.getCurrentUser().getId();
            tasks = taskRepository.findByAuthorId(currentUserId, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        List<TaskDTO> tasksDTOList = tasks.getContent()
                .stream()
                .map(task -> {
                    TaskDTO taskDTO = taskMapper.map(task);

                    LocalDate localDate = DurationConverter.convertDateToLocalDate(task.getCreatedAt());
                    taskDTO.setFormattedCreationDate(localDate);
                    return taskDTO;
                })
                .toList();

        return new PageImpl<>(tasksDTOList, pageable, tasks.getTotalElements());

    }

    public Page<Task> getMyTasks(Integer userId, Pageable pageable) {
        return taskRepository.findByAuthorId(userId, pageable);
    }

    public Optional<TaskDTO> getTaskById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) return Optional.empty();

        Task task = optionalTask.get();
        List<CommentDTO> commentDTOS = task.getComments()
                .stream()
                .map(commentMapper::map)
                .toList();
        TaskDTO taskDTO = taskMapper.map(task);
        taskDTO.setComments(commentDTOS);
        updateDurationToBeShown(taskDTO);

        return Optional.of(taskDTO);
    }

    public Optional<Task> getTaskByDescription(String description) {
        return taskRepository.findByDescription(description);
    }

    @Transactional
    public void save(TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.map(taskCreateDTO);
        task.setAuthor(CurrentUserUtil.getCurrentUser());
        ifTaskIsActiveAndSetDuration(task);
        taskRepository.save(task);
    }

    @Transactional
    public void update(int id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id).get();
        taskMapper.update(taskUpdateDTO, task);
        task.setAuthor(CurrentUserUtil.getCurrentUser());
        ifTaskIsActiveAndSetDuration(task);
        taskRepository.save(task);
    }

    @Transactional
    public void remove(int id) {
        taskRepository.deleteById(id);
    }

    private void ifTaskIsActiveAndSetDuration(Task task) {
        if (task.isActive()) {
            Instant updated = task.getUpdatedAt().toInstant();
            Instant now = Instant.now();
            long currentDuration = Duration.between(updated, now).toMillis();
            task.setDuration(task.getDuration() + currentDuration);
        }

        if (task.getStatus().name().equals("IN_PROGRESS")) {
            task.setActive(true);
        } else task.setActive(false);
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

