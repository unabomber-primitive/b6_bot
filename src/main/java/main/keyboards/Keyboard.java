package main.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    private final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    ArrayList<String> buttonsNames;

    public Keyboard(ArrayList<String> buttonsNames) {
        this.buttonsNames = buttonsNames;
        createInlineKeyboardMarkup();
    }

    public InlineKeyboardMarkup getMarkup() {
        return markup;
    }

    private void createInlineKeyboardMarkup() {
        markup.setKeyboard(fillStore());
    }

    protected List<List<InlineKeyboardButton>> fillStore() {
        List<List<InlineKeyboardButton>> store = new ArrayList<>();
        for (int i = 0;i < buttonsNames.size(); i++) {
            store.add(getRow(i));
        }
        return store;
    }

    protected List<InlineKeyboardButton> getRow(int rowNumber) {
        InlineKeyboardButton button = new InlineKeyboardButton(buttonsNames.get(rowNumber));
        button.setCallbackData(buttonsNames.get(rowNumber));

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }
}
