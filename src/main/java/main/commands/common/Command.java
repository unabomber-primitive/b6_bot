package main.commands.common;

import main.core.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command{
    default  Object execute(Update event) {
        return new SendMessage(event.getMessage().getChatId().toString(), "Скоро тут появится функционал");
    }

    default User setUserSettings(Update event) {
        if (event.hasCallbackQuery()) {
            return new User(
                    String.valueOf(event.getCallbackQuery().getFrom().getId()),
                    String.valueOf(event.getCallbackQuery().getMessage().getChatId())
            );
        }

        return new User(
                String.valueOf(event.getMessage().getFrom().getId()),
                String.valueOf(event.getMessage().getChatId())
        );
    }

    default SendMessage createErrorMessage(String chatId, String error) {
        return new SendMessage(chatId, error);
    }
}
