package main.taskshandler;

import main.bot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TasksSender implements Runnable{
    private final TelegramBot bot;

    public TasksSender(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object event = bot.sendQueue.take();
                if (event instanceof SendMessage) {
                    bot.execute((SendMessage) event);
                } else {
                    bot.execute((EditMessageReplyMarkup) event);
                }
            }
            catch (InterruptedException | TelegramApiException e) {
                e.getMessage();
            }
        }
    }
}
