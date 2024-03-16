package main;

import main.bot.TelegramBot;
import main.taskshandler.TasksHandler;
import main.taskshandler.TasksSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class B6BotApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(B6BotApplication.class, args);
		TelegramBot bot = new TelegramBot();
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(bot);
		new Thread(new TasksHandler(bot)).start();
		new Thread(new TasksSender(bot)).start();
	}

}
