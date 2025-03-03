package com.avdei.spring1app.service;

import com.avdei.spring1app.model.Status;
import com.avdei.spring1app.model.Task;
import com.avdei.spring1app.repository.TaskRepository;
import com.avdei.spring1app.util.CurrentUserUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> getMyTasks(Integer userId, Pageable pageable) {
        return taskRepository.findByAuthorId(userId, pageable);
    }

    public Optional<Task> getTaskById(int id) {

        return taskRepository.findById(id);
    }

    public Optional<Task> getTaskByDescription(String description) {
        return taskRepository.findByDescription(description);
    }

    @Transactional
    public void save(Task task) {
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
}
