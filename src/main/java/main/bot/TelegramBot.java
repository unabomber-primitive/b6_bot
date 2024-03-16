package main.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TelegramBot extends TelegramLongPollingBot {

    public final LinkedBlockingQueue<Object> sendQueue = new LinkedBlockingQueue<>();
    public final LinkedBlockingQueue<Update> receiveQueue = new LinkedBlockingQueue<>();

    @Override
    public void onUpdateReceived(Update update) {
        receiveQueue.add(update);

    }

    @Override
    public String getBotUsername() {
        return "st_b6_bot";
    }

    @Override
    public String getBotToken() {
        return "6838531694:AAGWmTigSx_BNWSaC9h7VRJCtuqvQta1SEs";
    }
}




































/*if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Добро пожаловать!");
            message.setReplyMarkup(new StudentStartKeyboard().getMarkup());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        else if (update.hasMessage()) {
            boolean result = simpleCommands.get("Гостевой пропуск").execute(update.getMessage().getFrom().getId().toString(), update.getMessage().getText());
            SendMessage message;
            if (result) {
                message = new SendMessage(update.getMessage().getChatId().toString(), "Запрос на гостевой пропуск оформлен");
            } else {
                message = new SendMessage(update.getMessage().getChatId().toString(), "Ошибка на сервере");
            }

            try {
                execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

        else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (simpleCommands.containsKey(data)) {
                SendMessage message = new SendMessage(chatId, "Введи информацию о госте");
                try {
                    execute(message);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else {
                EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder().chatId(chatId).messageId(messageId).build();

                try {
                    newKb.setReplyMarkup(new ServicesKeyboard().getMarkup());
                    execute(newKb);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
*/
