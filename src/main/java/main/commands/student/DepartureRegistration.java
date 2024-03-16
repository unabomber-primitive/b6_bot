package main.commands.student;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import static main.constants.ErrorMessages.*;
import main.core.User;
import main.database.DatabaseManager;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static main.constants.MessageFormats.*;
import static main.constants.StateInfo.SUCCESS;


public class DepartureRegistration implements Command {
    private User user;
    @Override
    public SendMessage execute(Update event) {
        setUp(event);

        if (event.hasMessage()) return completeQuery(event);
        return getAdditionInfo();
    }

    private void setUp(Update event) {
        if (user == null) user = setUserSettings(event);
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
    }

    public SendMessage getAdditionInfo() {
        return new SendMessage(user.chatId(), DATA_INFO.getFormat());
    }


    public SendMessage completeQuery(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId());

        String data = event.getMessage().getText();
        String[] param = data.split("\n");

        if (!ValidationService.validateDepartureInput(param)) return createErrorMessage(user.chatId(), INPUT_ERROR.getError());

        boolean success = DatabaseManager.createDepartureApplication(user.userId(), param[0]);

        if(!success) return createErrorMessage(user.chatId(), DATABASE_ERROR.getError());
        return new SendMessage(user.chatId(), SUCCESS.getState());
    }



}
