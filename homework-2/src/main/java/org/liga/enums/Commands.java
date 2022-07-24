package org.liga.enums;

import org.liga.exception.WrongCommandParametersException;
import org.liga.mapper.TaskMapper;
import org.liga.mapper.UserMapper;
import org.liga.model.Task;
import org.liga.model.User;
import org.liga.service.TaskService;
import org.liga.service.UserService;
import org.liga.util.StringConstants;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Commands {

    ALL_USERS {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 1;
            StringBuilder response = new StringBuilder();
            if (parameters.size() == requiredAmountOfParams) {
                List<User> users = userService.findAll();
                for (User user : users) {
                    response.append(user.toString()).append(System.lineSeparator());
                }
            } else {
                throw new WrongCommandParametersException();
            }
            return response.toString();
        }
    },

    ALL_TASKS {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParamsToFindAll = 1;
            int requiredAmountOfParamsToFindAllByStatus = 2;
            StringBuilder response = new StringBuilder();
            int size = parameters.size();
            List<Task> tasks;
            if (size == requiredAmountOfParamsToFindAll) {
                tasks = taskService.findAll();
            } else if (size == requiredAmountOfParamsToFindAllByStatus) {
                String status = parameters.get(1).trim();
                tasks = taskService.findAllByStatus(Status.valueOf(status));
            } else {
                throw new WrongCommandParametersException();
            }
            for (Task task : tasks) {
                User user = task.getUser();
                response.append(user).append(" - ").append(task).append(System.lineSeparator());
            }
            return response.toString();
        }
    },

    NEW_USER {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 3;
            int size = parameters.size();
            String response = "";
            if (size == requiredAmountOfParams) {
                String parametersLine = parameters.stream()
                        .skip(1)
                        .collect(Collectors.joining(","));
                User userFromString = UserMapper.stringToUser(parametersLine);
                Optional<User> user = userService.create(userFromString);
                response = "Пользователь добавлен: " + user.orElseThrow(WrongCommandParametersException::new);
            } else {
                throw new WrongCommandParametersException();
            }
            return response;
        }
    },

    NEW_TASK {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParamsWithoutStatus = 5;
            int requiredAmountOfParamsWithStatus = 6;
            int size = parameters.size();
            String response = "";
            if (size == requiredAmountOfParamsWithoutStatus || size == requiredAmountOfParamsWithStatus) {
                String parametersLine = parameters.stream()
                        .skip(1)
                        .collect(Collectors.joining(","));
                Task taskFromString = TaskMapper.stringToTask(parametersLine);
                Optional<Task> task = taskService.create(taskFromString);
                response = "Задача добавлена: " + task.orElseThrow(WrongCommandParametersException::new);
            } else {
                throw new WrongCommandParametersException();
            }
            return response;
        }
    },

    GET_USER {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 2;
            if (parameters.size() != requiredAmountOfParams) {
                throw new WrongCommandParametersException();
            }
            int id = Integer.parseInt(parameters.get(1).trim());
            User founded = userService.findById(id).orElseThrow(EntityNotFoundException::new);
            return "UPDATE_USER, " + UserMapper.userToString(founded);
        }
    },

    GET_TASK {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 2;
            if (parameters.size() != requiredAmountOfParams) {
                throw new WrongCommandParametersException();
            }
            int id = Integer.parseInt(parameters.get(1).trim());
            Task founded = taskService.findById(id).orElseThrow(EntityNotFoundException::new);
            return "UPDATE_TASK, " + TaskMapper.taskToString(founded);
        }
    },

    UPDATE_USER {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 4;
            int size = parameters.size();
            String response = "";
            if (size == requiredAmountOfParams) {
                int id = Integer.parseInt(parameters.get(1).trim());
                String parametersLine = parameters.stream()
                        .skip(2)
                        .collect(Collectors.joining(","));
                User userFromString = UserMapper.stringToUser(parametersLine);
                Optional<User> user = userService.update(id, userFromString);
                response = "Пользователь обновлен: " + user.orElseThrow(WrongCommandParametersException::new);
            }
            return response;
        }
    },

    UPDATE_TASK {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 6;
            int size = parameters.size();
            String response = "";
            if (size == requiredAmountOfParams) {
                int id = Integer.parseInt(parameters.get(1).trim());
                String parametersLine = parameters.stream()
                        .skip(2)
                        .collect(Collectors.joining(","));
                Task newTask = TaskMapper.stringToTask(parametersLine);
                Optional<Task> task = taskService.update(id, newTask);
                response = "Задача обновлена: " + task.orElseThrow(WrongCommandParametersException::new);
            }
            return response;
        }
    },

    DELETE_ALL_USERS {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 1;
            if (parameters.size() == requiredAmountOfParams) {
                userService.deleteAll();
            } else {
                throw new WrongCommandParametersException();
            }
            return "Все пользователи удалены";
        }
    },

    DELETE_ALL_TASKS {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 1;
            if (parameters.size() == requiredAmountOfParams) {
                taskService.deleteAll();
            } else {
                throw new WrongCommandParametersException();
            }
            return "Все задачи удалены";
        }
    },

    DELETE_USER {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 2;
            String response;
            if (parameters.size() == requiredAmountOfParams) {
                int id = Integer.parseInt(parameters.get(1).trim());
                userService.deleteById(id);
                response = "Пользователь с id = " + id + " удален";
            } else {
                throw new WrongCommandParametersException();
            }
            return response;
        }
    },

    DELETE_TASK {
        String response = "";

        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 2;
            if (parameters.size() == requiredAmountOfParams) {
                int id = Integer.parseInt(parameters.get(1).trim());
                taskService.deleteById(id);
                response = "Задача с id = " + id + " удалена";
            } else {
                throw new WrongCommandParametersException();
            }
            return response;
        }
    },

    CHANGE_STATUS {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            int requiredAmountOfParams = 3;
            int size = parameters.size();
            String response;
            if (size == requiredAmountOfParams) {
                int id = Integer.parseInt(parameters.get(1).trim());
                String status = parameters.get(2).trim();
                taskService.changeStatus(id, Status.valueOf(status));
                response = "Статус обновлен";
            } else {
                throw new WrongCommandParametersException();
            }
            return response;
        }
    },
    HELP {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            return StringConstants.HELP_TEXT;
        }
    },
    EXIT {
        @Override
        public String action(UserService userService, TaskService taskService, List<String> parameters) {
            return StringConstants.GOODBYE_MESSAGE;
        }
    };

    public abstract String action(UserService userService, TaskService taskService, List<String> parameters);
}
