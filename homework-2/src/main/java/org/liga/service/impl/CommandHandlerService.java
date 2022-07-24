package org.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.liga.enums.Commands;
import org.liga.exception.WrongCommandParametersException;
import org.liga.service.TaskService;
import org.liga.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final UserService userService;
    private final TaskService taskService;

    public String handleCommand(String inputCommand) {

        String response;

        List<String> commandParameters = Arrays.asList(
                inputCommand.trim().split(","));

        String command = commandParameters.get(0).trim();
        try {
            response = Commands.valueOf(command).action(userService, taskService, commandParameters);
        } catch (WrongCommandParametersException e) {
            response = e.getMessage() + "пожалуйста, попробуйте еще раз";
        } catch (IllegalArgumentException e) {
            response = e + "Команда не распознается, попробуйте еще раз";
        } catch (RuntimeException e){
            response = e + "Упс, что-то сломалось...";
        }
        return response;
    }
}
