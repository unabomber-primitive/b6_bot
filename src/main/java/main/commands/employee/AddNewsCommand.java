package main.commands.employee;

import main.commands.common.Command;
import main.commands.common.UserCommandsStore;
import main.core.User;
import main.database.DatabaseManager;
import main.services.ValidationService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static main.constants.ErrorMessages.*;
import static main.constants.MessageFormats.*;
import static main.constants.Roles.DORM_DIRECTOR;


public class AddNewsCommand implements Command {
    int iteration = 0;
    private User user;
    @Override
    public Object execute(Update event) {
        setUpCommand(event);

        iteration++;
        return switch(iteration) {
            case 1 -> getNews();
            case 2 -> sendNews(event);
            default -> null;
        };
    }

    private void setUpCommand(Update event) {
        if(user == null) user = setUserSettings(event);
        UserCommandsStore.lastUserCommand.put(user.userId(), this);
    }

    private SendMessage getNews() {
        if (!ValidationService.hasRights(user.userId(), DORM_DIRECTOR.getRole())) {
            UserCommandsStore.lastUserCommand.remove(user.userId());
            return createErrorMessage(user.userId(), PERMISSION_ERROR.getError());
        }

        return new SendMessage(user.userId(), COMMON.getFormat() + NEWS_INFO.getFormat());
    }

    private SendMessage sendNews(Update event) {
        UserCommandsStore.lastUserCommand.remove(user.userId());
        String news = event.getMessage().getText();

        StringBuilder header = new StringBuilder();
        for (int i = 0; i < news.length(); i++) {
            if (news.charAt(i) == '\n') continue;
            header.append(news.charAt(i));
        }

        boolean success = DatabaseManager.addNews(user.userId(), header.toString(), news);

        if (success) {
            return new SendMessage("@dorm_b6_news", event.getMessage().getText());
        }

        return createErrorMessage(user.userId(), DATABASE_ERROR.getError());
    }
}
