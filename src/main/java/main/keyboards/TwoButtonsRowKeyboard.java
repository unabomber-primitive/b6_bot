package main.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TwoButtonsRowKeyboard extends Keyboard{
    public TwoButtonsRowKeyboard(ArrayList<String> buttonsNames) {
        super(buttonsNames);
    }
    protected List<List<InlineKeyboardButton>> fillStore() {
        List<List<InlineKeyboardButton>> store = new ArrayList<>();
        for (int i = 0; i < (super.buttonsNames.size() + 1) / 2; i++) {
            store.add(getRow(i));
        }
        return store;
    }

    protected List<InlineKeyboardButton> getRow(int rowNumber) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        int it = rowNumber * 2;
        while (it < buttonsNames.size() && row.size() < 2) {
            InlineKeyboardButton button = new InlineKeyboardButton(buttonsNames.get(it));
            button.setCallbackData(buttonsNames.get(it++));
            row.add(button);
        }

        return row;
    }
}
