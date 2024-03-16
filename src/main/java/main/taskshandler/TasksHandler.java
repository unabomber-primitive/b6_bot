package main.taskshandler;

import main.bot.TelegramBot;
import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.utils.CommandFabric;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static main.constants.ErrorMessages.UNKNOWN_COMMAND;

public class TasksHandler implements Runnable{
    private static TelegramBot bot;
    private final ExecutorService pool;
    private final CommandFabric commandFabric;

    public TasksHandler(TelegramBot obj) {
        bot = obj;
        pool = Executors.newCachedThreadPool();
        commandFabric = new CommandFabric();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Update event = bot.receiveQueue.take();
                String text = event.hasMessage() ? event.getMessage().getText() : event.getCallbackQuery().getData();
                Long userId = event.hasMessage() ? event.getMessage().getFrom().getId() : event.getCallbackQuery().getFrom().getId();

                Command command = commandFabric.getCommand(text);
                Command lastCommand = UserCommandsStore.lastUserCommand.getOrDefault(String.valueOf(userId), null);
                Command resultCommand = command == null ? lastCommand : command;

                System.out.println(text);

                if (resultCommand == null) {
                    bot.sendQueue.add(new SendMessage(String.valueOf(event.getMessage().getChatId()), UNKNOWN_COMMAND.getError()));
                    continue;
                }

                pool.submit(() -> {
                    Object message = resultCommand.execute(event);
                    bot.sendQueue.add(message);
                });
            }
            catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    public static void massMailing(ArrayList<SendMessage> messages) {
        bot.sendQueue.addAll(messages);
    }


}
