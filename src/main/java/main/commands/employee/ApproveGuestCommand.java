package main.commands.employee;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.constants.ErrorMessages;
import main.core.Application;
import main.core.User;
import main.database.DatabaseManager;
import main.keyboards.Keyboard;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static main.constants.ErrorMessages.*;
import static main.constants.Roles.DORM_DIRECTOR;
import static main.taskshandler.TasksHandler.massMailing;
import static main.constants.StateInfo.*;

public class ApproveGuestCommand implements Command {
    private int iteration = 0;
    private User user;
    private ArrayList<Application> applications;

    @Override
    public Object execute(Update event) {
        setUp(event);

        iteration++;
        return switch (iteration) {
            case 1 -> getAllApplications();
            case 2 -> confirmGuests();
            default -> null;
        };
    }

    private void setUp(Update event) {
        if (user == null) user = setUserSettings(event);
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
    }

    private SendMessage getAllApplications() {
        if (!ValidationService.hasRights(user.userId(), DORM_DIRECTOR.getRole())) {
            UserCommandsStore.lastUserCommand.remove(user.userId());
            return createErrorMessage(user.chatId(), ErrorMessages.PERMISSION_ERROR.getError());
        }

        applications = DatabaseManager.getApplicationWaitingForConfirmation();

        String str = formatMessage();

        if (str.isEmpty()) {
            UserCommandsStore.lastUserCommand.remove(user.userId());
            return new SendMessage(user.chatId(), NO_DATA.getState());
        }

        SendMessage message = new SendMessage(user.chatId(), str);
        message.setReplyMarkup(new Keyboard(new ArrayList<>(List.of("Одобрить"))).getMarkup());

        return message;
    }

    private String formatMessage() {
        StringBuilder str = new StringBuilder();
        for (Application i : applications) {
            str.append(i);
            str.append("\n\n");
        }

        return str.toString();
    }

    private SendMessage confirmGuests() {
        UserCommandsStore.lastUserCommand.remove(user.chatId());

        boolean isConfirmationSuccess = DatabaseManager.confirmApplications();
        if (!isConfirmationSuccess) return createErrorMessage(user.chatId(), DATABASE_ERROR.getError());

        ArrayList<SendMessage> messages = new ArrayList<>();
        for (Application i : applications) {
            String studentChatId = DatabaseManager.chatIdByCardNumber(i.cardNumber());
            messages.add(new SendMessage(studentChatId, GUEST_APPROVED.getState()));
        }

        massMailing(messages);
        return new SendMessage(String.valueOf(user.chatId()), SUCCESS.getState());
    }
}
