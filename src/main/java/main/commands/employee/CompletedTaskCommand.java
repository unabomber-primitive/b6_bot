package main.commands.employee;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.constants.ErrorMessages;
import main.core.User;
import main.database.DatabaseManager;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static main.constants.MessageFormats.*;
import static main.constants.Roles.DORM_DIRECTOR;
import static main.constants.StateInfo.SUCCESS;

public class CompletedTaskCommand implements Command {
    private User user;
    int iteration = 0;
    @Override
    public SendMessage execute(Update event) {
        setUp(event);

        if (ValidationService.hasRights(user.userId(), DORM_DIRECTOR.getRole()))
            return createErrorMessage(user.chatId(), ErrorMessages.PERMISSION_ERROR.getError());

        iteration++;
        return switch(iteration) {
            case 1 -> getCompletedTasks();
            case 2 -> addCompletedTasks(event);
            default ->  null;
        };
    }

    private void setUp(Update event) {
        if (user == null) user = setUserSettings(event);
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
    }

    private SendMessage getCompletedTasks() {
        return new SendMessage(user.chatId(), COMMON.getFormat() + COMPLETED_TASK_INFO.getFormat());
    }

    private SendMessage addCompletedTasks(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId(), this);
        String[] tasksByEmployee = event.getMessage().getText().split("\n\n");

        int cnt = 1;
        StringBuilder str = new StringBuilder();
        StringBuilder errorInsert = new StringBuilder();
        for (String i: tasksByEmployee) {
            if (str.isEmpty()) str.append("Ошибочные данные в строках: ");
            String[] task = i.split("\n");
            if (!ValidationService.validateTaskInput(task)) {
                str.append(cnt);
                continue;
            }

            for (String j: task[1].split(", ")) {
                if (errorInsert.isEmpty()) errorInsert.append("О сотрудниках, указанных на этих строках, не удалось добавить информацию в базу данных. Попробуйте позже: ");
                boolean success = DatabaseManager.insertIntoCompletedTasks(task[0], Integer.valueOf(j));
                if (!success) errorInsert.append(cnt);
            }
        }


        if (str.isEmpty() && errorInsert.isEmpty()) return new SendMessage(user.chatId(), SUCCESS.getState());

        return new SendMessage(user.chatId(), str + "\n" + errorInsert);
    }


}
