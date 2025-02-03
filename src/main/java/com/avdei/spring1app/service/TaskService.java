package com.avdei.spring1app.service;

import com.avdei.spring1app.domain.Task;
import com.avdei.spring1app.repository.TaskRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Cacheable(value = "users", key = "#id")
    public Optional<Task> getTaskById(int id) {

        return taskRepository.findById(id);
    }
    public Optional<Task> getTaskByDescription(String description){
        return taskRepository.findByDescription(description);
    }

    @Transactional
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void remove(int id) {
        taskRepository.deleteById(id);
    }
}
