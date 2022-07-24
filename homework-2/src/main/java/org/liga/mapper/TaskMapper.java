package org.liga.mapper;

import org.liga.enums.Status;
import org.liga.exception.WrongCommandParametersException;
import org.liga.model.Task;
import org.liga.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TaskMapper {

    public static Task stringToTask(String taskParametersLine) {
        Task task;
        List<String> parameters = Arrays.stream(
                taskParametersLine.split(",")
        ).map(String::trim)
                .toList();
        if (parameters.size() < 4) {
            throw new WrongCommandParametersException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            int userId = Integer.parseInt(parameters.get(2));
            User user = new User();
            user.setId(userId);
            task = Task.builder()
                    .name(parameters.get(0))
                    .description(parameters.get(1))
                    .user(user)
                    .deadline(LocalDate.parse(parameters.get(3), formatter))
                    .build();
            if (parameters.size() == 5) {
                task.setStatus(Status.valueOf(parameters.get(4)));
            } else {
                task.setStatus(Status.NEW);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new WrongCommandParametersException("Неверное количество параметров");
        }

        return task;
    }

    public static String taskToString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return task.getId() + ", "
                + task.getName() + ", "
                + task.getDescription() + ", "
                + task.getUser() + ", "
                + task.getDeadline().format(formatter) + ", "
                + task.getStatus();
    }

}
