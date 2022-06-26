package org.liga;

import org.liga.service.CommandHandler;

import java.util.Scanner;

public class TaskTrackerApp {
    private static final String GREETINGS = """
            Привет! Это консольное приложение Таск-трекер.
            Работа с приложением осуществляется через консоль.
                       
            """;
    private static final String HELP = """
            Вводите команды согласно данной инструкции.
            ВНИМАНИЕ! используйте запятые только для разделения параметров запроса.
            Не используйте запятые в параметрах запроса (например в описании задачи).

            * HELP - вывод данной инструкции
            * ALL_TASKS - вывод всех имеющихся заданий
                
            * Создание нового задания:
                NEW_TASK, [название задания], [описание задания], [ид пользователя], [дата окончания]
                Например: NEW_TASK, новое задание, создать новое задание, 1, 22.06.2022
                
            * GET_TASK, [номер задачи] - Просмотр задачи по номеру
            * CHANGE_STATUS, [номер задания], [новый статус] - изменение статуса задачи
                Статус может быть: NEW / IN_PROGRESS / DONE
                При создании задания присваивается статус NEW по-умолчанию.
                
            * Для редактирования задачи введите:
                UPDATE_TASK, [номер задачи], [новое название задания],
                [новое описание задания], [новый ид пользователя], [новая дата окончания]
                p.s. Для удобства введите: GET_TASK, [номер задачи]
                и в ответ получите задачу в виде готового запроса для редактирования, например:
                UPDATE_TASK, 1, новое задание, создать новое задание, 1, 22.06.2022
                Скопируйте его, замените нужные поля и нажмите Enter.
                
            * DELETE_TASK, [номер задания] - удаление задания по номеру
            * DELETE_ALL_TASKS - удаления всех заданий
            * ALL_TASKS, [статус задания] - вывод всех пользователей, офильтрованных по статусу задания
                Например: `ALL_TASKS, NEW`
                Пример вывода:
                1. Иван Иванов - 1. Решить задачу: Решить поставленную задачу (новое) - 22.06.2022
                1. Иван Иванов - 2. Создать задание: Создать новое задание (новое) - 22.06.2022
                2. Пётр Петров - 3. Выполнить ДЗ: Придумать и написать кучу кода (новое) - 23.06.2022
            * ALL_USERS - вывод всех имеющихся пользователей
            * NEW_USER, [имя], [фамилия] - добавление нового пользователя
            * GET_USER, [ид пользователя] - Просмотр задачи по номеру.
                        
            * UPDATE_USER, [ид пользователя], [имя], [фамилия] - обновление данных пользователя
                p.s. Для удобства введите: `GET_USER, [ид пользователя]
                и в ответ получите пользователя в виде готового запроса для редактирования, например:
                UPDATE_USER, 1, Иван, Иванов
                Скопируйте его, замените нужные поля и нажмите Enter.
            * DELETE_USER, [ид пользователя] - удаление пользователя по ид
            * DELETE_ALL_USERS - удаление всех пользователей
            * EXIT - завершение программы
                        
            Введите команду или введите EXIT для завершения программы.
            """;
    private static final String MESSAGE_AFTER_COMMAND_EXECUTING =
            "Введите команду, введите `HELP` для вывода инструкции или введите `EXIT` для завершения программы";
    private static final String GOODBYE_MESSAGE = """
            До свидания, надеюсь, приложение было полезно!
            Все вопросы и пожелания можно направить сюда: marishka.ant@gmail.com
            """;

    static {
        System.out.println(GREETINGS + HELP);
    }

    public static void main(String[] args) {

        CommandHandler commandHandler = CommandHandler.getInstance();
        Scanner sc = new Scanner(System.in).useDelimiter("\\n");
        boolean isAppClosingNeed = false;
        do {
            String command = sc.next();
            if (command.equalsIgnoreCase("EXIT")) {
                isAppClosingNeed = true;
            } else if (command.equalsIgnoreCase("HELP")) {
                System.out.println(HELP);
            } else {
                commandHandler.handleCommand(command);
                System.out.println(MESSAGE_AFTER_COMMAND_EXECUTING);
            }
        }
        while (!isAppClosingNeed && sc.hasNext());
        sc.close();

        System.out.println(GOODBYE_MESSAGE);
    }
}
