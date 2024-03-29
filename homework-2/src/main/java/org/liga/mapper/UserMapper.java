package org.liga.mapper;

import org.liga.exception.WrongCommandParametersException;
import org.liga.model.User;

import java.util.Arrays;
import java.util.List;

public class UserMapper {

    public static User stringToUser(String userParametersLine) {
        User user;
        List<String> parameters = Arrays.stream(
                userParametersLine.split(",")
        ).map(String::trim)
                .toList();
        if (parameters.size() < 2) {
            throw new WrongCommandParametersException();
        }
        try {
            user = User.builder()
                    .firstName(parameters.get(0))
                    .lastName(parameters.get(1))
                    .build();
        } catch (NumberFormatException e) {
            throw new WrongCommandParametersException("Неверное количество параметров");
        }

        return user;
    }

    public static String userToString(User user) {
        return user.getId() + ", "
                + user.getFirstName() + ", "
                + user.getLastName();
    }

    public static User csvStringToUser(String csvUserParametersLine) {
        User user;
        List<String> parameters = Arrays.stream(
                        csvUserParametersLine.split(",")
                ).map(String::trim)
                .toList();
        if (parameters.size() < 3) {
            throw new WrongCommandParametersException();
        }
        try {
            user = User.builder()
                    .id(Integer.parseInt(parameters.get(0)))
                    .firstName(parameters.get(1))
                    .lastName(parameters.get(2))
                    .build();
        } catch (NumberFormatException e) {
            throw new WrongCommandParametersException("Неверное количество параметров");
        }

        return user;
    }

}
