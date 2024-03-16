package main.commands.common;

import main.constants.EmployeeOrStudentButtons;
import main.constants.StartEmployeeButtonsName;
import main.constants.StartStudentButtonsName;
import main.core.User;
import main.database.DatabaseManager;
import main.keyboards.TwoButtonsRowKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static main.constants.ErrorMessages.PERMISSION_ERROR;
import static main.constants.Roles.STUDENT;

public class Start implements Command {
    private User user;
    private int iterationNumber = 0;
    @Override
    public Object execute(Update event) {
        setUp(event);

        iterationNumber++;
        return switch(iterationNumber) {
            case 1 -> whichRights(event);
            case 2 -> sendOptions(event);
            default -> null;
        };
    }

    private void setUp(Update event) {
        if (user == null) user = setUserSettings(event);
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
    }

    private SendMessage whichRights(Update event) {
        SendMessage message = new SendMessage(event.getMessage().getChatId().toString(), "Привет");
        message.setReplyMarkup(new TwoButtonsRowKeyboard(EmployeeOrStudentButtons.getButtonsNames()).getMarkup());

        return message;
    }

    private Object sendOptions(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId());

        String role = event.getCallbackQuery().getData();
        String realRole = DatabaseManager.isStudentOrEmployee(user.userId());

        if (!role.equals(realRole)) return createErrorMessage(user.chatId(), PERMISSION_ERROR.getError());

        return createKeyboard(realRole.equals(STUDENT.getRole()), event);
    }

    private EditMessageReplyMarkup createKeyboard(boolean isStudent, Update event) {
        int messageId = event.getCallbackQuery().getMessage().getMessageId();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder().chatId(user.chatId()).messageId(messageId).build();
        newKb.setReplyMarkup(new TwoButtonsRowKeyboard(chooseButtons(isStudent)).getMarkup());

        return newKb;
    }

    private ArrayList<String> chooseButtons(boolean isStudent) {
        return isStudent ? StartStudentButtonsName.getButtonsNames() : StartEmployeeButtonsName.getButtonsNames();
    }
}
