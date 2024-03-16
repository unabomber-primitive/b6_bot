package main.commands.student;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.core.User;
import main.database.DatabaseManager;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static main.constants.MessageFormats.*;
import static main.constants.ErrorMessages.*;
import static main.constants.StateInfo.SUCCESS;

public class GuestInvitation implements Command {
    private User user;
    @Override
    public SendMessage execute(Update event) {
        if (user == null) user = setUserSettings(event);
        if (ValidationService.isCardBlocked(user.userId())) return createErrorMessage(user.chatId(), BLOCKED_CARD.getError());
        if (event.hasMessage()) return completeQuery(event);
        return getAdditionInfo();
    }

    private SendMessage getAdditionInfo() {
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
        return new SendMessage(user.chatId(), GUEST_INFO.getFormat());
    }


    private SendMessage completeQuery(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId());

        String data = event.getMessage().getText();
        String[] param = data.split("\n");

        if (!ValidationService.validateGuestInput(param)) return createErrorMessage(user.chatId(), INPUT_ERROR.getError());

        boolean success = DatabaseManager.createGuestApplication(user.userId(), param);

        if (!success) createErrorMessage(user.chatId(), DATABASE_ERROR.getError());
        return new SendMessage(user.chatId(), SUCCESS.getState());
    }



}
