package main.commands.employee;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.core.DeparturePersonInfo;
import main.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static main.constants.StateInfo.NO_DATA;

public class WhoHasLeftCheck implements Command {

    @Override
    public SendMessage execute(Update event) {
        String message = formatMessage(DatabaseManager.getWhoHasLeft());

        return new SendMessage(String.valueOf(event.getCallbackQuery().getMessage().getChatId()), message);
    }

    private String formatMessage(ArrayList<DeparturePersonInfo> people) {
        StringBuilder str = new StringBuilder();
        for (DeparturePersonInfo i : people) {
            str.append(i);
            str.append("\n\n");
        }

        if (str.isEmpty()) return NO_DATA.getState();

        return str.toString();
    }
}
