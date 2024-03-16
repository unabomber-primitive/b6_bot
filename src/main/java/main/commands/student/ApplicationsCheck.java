package main.commands.student;

import main.commands.common.Command;
import main.core.Task;
import main.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static main.constants.StateInfo.NO_DATA;


public class ApplicationsCheck implements Command {
    @Override
    public SendMessage execute(Update event) {
        String userId = String.valueOf(event.getCallbackQuery().getFrom().getId());

        String message = formatMessage(DatabaseManager.getRequests(userId));

        return new SendMessage(String.valueOf(event.getCallbackQuery().getMessage().getChatId()), message);
    }

    private String formatMessage(ArrayList<Task> tasks) {
        StringBuilder str = new StringBuilder();
        for (Task i : tasks) {
            str.append(i);
            str.append("\n\n");
        }

        if (str.isEmpty()) return NO_DATA.getState();

        return str.toString();
    }
}
