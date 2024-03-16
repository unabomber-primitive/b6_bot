package main.commands.student;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.constants.ErrorMessages;
import main.constants.ServicesConstData;
import main.core.Service;
import main.core.User;
import main.database.DatabaseManager;
import main.keyboards.Keyboard;
import main.keyboards.TwoButtonsRowKeyboard;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static main.constants.ErrorMessages.DATABASE_ERROR;
import static main.constants.StateInfo.SUCCESS;

public class ServiceChoose implements Command {
    private final HashMap<String, Service> services;
    private int iterationNumber = 0;
    private String serviceId;
    private User user;
    private String date;
    private String hour;
    private String problemDescription;


    public ServiceChoose() {
        services = ServicesConstData.services;
    }

    public ArrayList<String> getServiceDescription() {
        ArrayList<String> res = new ArrayList<>();
        for (Service i: services.values()) {
            res.add(i.toString());
        }
        Collections.sort(res);
        return res;
    }

    @Override
    public Object execute(Update event) {
        if (user == null) user = setUserSettings(event);

        if (ValidationService.isCardBlocked(user.userId()))
            return createErrorMessage(user.chatId(), ErrorMessages.BLOCKED_CARD.getError());

        UserCommandsStore.lastUserCommand.put(user.userId(), this);
        iterationNumber++;
        return switch (iterationNumber) {
            case 1 -> chooseService(event);
            case 2 -> getAvailableDates(event);
            case 3 -> getAvailableHours(event);
            case 4 -> getProblemDescription(event);
            case 5 -> makeBooking(event);
            default -> null;
        };
    }

    private EditMessageReplyMarkup chooseService(Update event) {
        int messageId = event.getCallbackQuery().getMessage().getMessageId();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder().chatId(user.chatId()).messageId(messageId).build();
        newKb.setReplyMarkup(new Keyboard(getServiceDescription()).getMarkup());

        return newKb;
    }

    private EditMessageReplyMarkup getAvailableDates(Update event) {
        int messageId = event.getCallbackQuery().getMessage().getMessageId();

        serviceId = event.getCallbackQuery().getData().split(" - ")[0];

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder().chatId(user.chatId()).messageId(messageId).build();
        newKb.setReplyMarkup(new TwoButtonsRowKeyboard(services.get(serviceId).getAvailableDate()).getMarkup());

        return newKb;
    }

    private EditMessageReplyMarkup getAvailableHours(Update event) {
        int messageId = event.getCallbackQuery().getMessage().getMessageId();

        date = event.getCallbackQuery().getData();
        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder().chatId(user.chatId()).messageId(messageId).build();
        newKb.setReplyMarkup(new TwoButtonsRowKeyboard(services.get(serviceId).getAvailableHours(date)).getMarkup());

        return newKb;
    }

    public SendMessage getProblemDescription(Update event) {
        hour = event.getCallbackQuery().getData();
        if (!services.get(serviceId).addHour(date, hour)) {
            return new SendMessage(event.getMessage().getChatId().toString(), "Что - то пошло не так, попробуйте еще раз");
        }

        return new SendMessage(user.chatId(), "Введите описание проблемы");
    }


    private SendMessage makeBooking(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId());
        problemDescription = event.getMessage().getText();

        boolean success = DatabaseManager.createTask(user.userId(), serviceId, problemDescription, date, hour);

        if(!success) createErrorMessage(user.chatId(), DATABASE_ERROR.getError());
        return new SendMessage(user.chatId(), SUCCESS.getState());
    }
}
