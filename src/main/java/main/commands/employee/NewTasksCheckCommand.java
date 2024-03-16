package main.commands.employee;

import main.commands.common.Command;
import main.core.EmployeeTask;
import main.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static main.constants.StateInfo.NO_DATA;

public class NewTasksCheckCommand implements Command {
    @Override
    public Object execute(Update event) {
        ArrayList<EmployeeTask> ans = DatabaseManager.getTasks();

        StringBuilder str = new StringBuilder();
        for (EmployeeTask i : ans) {
            str.append(i);
            str.append("\n\n");
        }

        if (str.isEmpty()) str = new StringBuilder(NO_DATA.getState());

        return new SendMessage(String.valueOf(event.getCallbackQuery().getMessage().getChatId()), str.toString());
    }
}
