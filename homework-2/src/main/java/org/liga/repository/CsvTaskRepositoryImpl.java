package org.liga.repository;

import lombok.NonNull;
import org.liga.enums.Status;
import org.liga.exception.WrongCommandParametersException;
import org.liga.mapper.TaskMapper;
import org.liga.model.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;
import static org.liga.util.StringConstants.*;

@Repository
@Qualifier("CsvTaskRepository")
public class CsvTaskRepositoryImpl implements TaskRepository {

    private List<String> lines = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    private final String stringPath = "src/test/resources/Tasks.csv";
    private final Path path = Path.of(stringPath);

    public CsvTaskRepositoryImpl() {
        try {
            initialiseTasks();
        } catch (IOException e) {
            System.out.println(FILE_READING_ERROR + e.getMessage());
        }
    }

    private void initialiseTasks() throws IOException {
        lines.addAll(Files.readAllLines(path));
        if (lines.size() > 0) {
            tasks.addAll(lines.stream()
                    .map(TaskMapper::stringToTask)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public Task save(@NonNull Task task) {
        int nextId = getNextId();
        if (!validate(task)) {
            System.out.println("Все параметры, кроме статуса, должны быть заполнены");
            return null;
        }
        if (findById(task.getId()).isPresent()) {
            return update(task);
        }
        String line = TaskMapper.taskToString(task);
        lines.add(line);
        tasks.add(task);
        try {
            Files.write(path, List.of(line), CREATE, APPEND);
        } catch (IOException e) {
            System.out.println("Не удалось записать задачу в файл - " + e);
            return null;
        }
        return task;
    }

    @Override
    public <S extends Task> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public List<Task> findAll() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findAllById(Iterable<Integer> ids) {
        List<Task> foundedTasks = new ArrayList<>();
        ids.forEach(i -> foundedTasks.add(
                findById(i).orElseThrow(EntityNotFoundException::new)
                )
        );
        return foundedTasks;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findAny();
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public List<Task> findByStatus(Status status) {
        return tasks.stream()
                .filter(t -> t.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteAll() {
        lines.clear();
        tasks.clear();
        try {
            Files.write(path, new ArrayList<>(), TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(IMPOSSIBLE_TO_DELETE + path, e);
        }

    }

    @Override
    public void deleteById(Integer id) {
        Task founded = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findAny().orElseThrow(() ->
                        new WrongCommandParametersException(TASK_NOT_FOUND));
        tasks.removeAll(List.of(founded));
        lines.removeAll(List.of(TaskMapper.taskToString(founded)));
        try {
            Files.write(path, lines, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(IMPOSSIBLE_TO_DELETE + path, e);
        }
    }

    @Override
    public void delete(Task task) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {

    }

    public Task update(Task task) {
        List<Task> updatedTasks = tasks.stream()
                .map(t -> {
                    if (t.getId().equals(task.getId())) {
                        lines.remove(TaskMapper.taskToString(t));
                        lines.add(TaskMapper.taskToString(task));
                        t.setName(task.getName());
                        t.setDescription(task.getDescription());
                        t.setUser(task.getUser());
                        t.setDeadline(task.getDeadline());
                        t.setStatus(task.getStatus());
                    }
                    return t;
                })
                .collect(Collectors.toList());
        tasks.clear();
        tasks.addAll(updatedTasks);
        try {
            Files.write(path, lines, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(IMPOSSIBLE_TO_DELETE + path, e);
        }
        return task;
    }

    public void changeStatus(Integer id, String status) {
        Task founded = findById(id).orElseThrow(WrongCommandParametersException::new);
        founded.setStatus(Status.valueOf(status));
        update(founded);
    }

    private Boolean validate(Task task) {
        return task.getId() != null &&
                task.getName() != null &&
                task.getDescription() != null &&
                task.getUser() != null &&
                task.getDeadline() != null;
    }


    private int getNextId() {
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
        int nextId = 1;
        if(tasks.size() > 0) {
            nextId = (sortedTasks.get(tasks.size() - 1).getId()) + 1;
        }
        return nextId;
    }
}

