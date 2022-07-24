package org.liga.service.impl;

import org.liga.repository.TaskRepository;
import org.liga.enums.Status;
import org.liga.model.Task;
import org.liga.service.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Qualifier(value = "TaskRepository")
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAllByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void changeStatus(Integer id, Status status) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>((Collection<? extends Task>) taskRepository.findAll());
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    @Override
    public Optional<Task> create(Task task) {
        return Optional.of(taskRepository.save(task));
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    @Override
    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Task> update(Integer id, Task newTask) {
        taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        newTask.setId(id);
        return Optional.of(taskRepository.save(newTask));
    }
}
