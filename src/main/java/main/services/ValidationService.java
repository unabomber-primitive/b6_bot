package main.services;

import main.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static main.constants.ErrorMessages.INPUT_ERROR;
import static main.constants.NumericConst.DEPARTURE_INPUT_ROWS;
import static main.constants.NumericConst.GUEST_INPUT_ROWS;

public class ValidationService {
    public static boolean isCardBlocked(String userId) {
        return DatabaseManager.isCardBlocked(userId);
    }
    public static boolean hasRights(String userId, String role) {
        return DatabaseManager.hasRights(userId, role);
    }

    public static boolean checkDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean checkTimeFormat(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            LocalTime.parse(time, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validateTaskInput(String[] task) {
        return task.length == 2 && checkPhoneNumber(task[0]) && checkTask(task[1]);
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        int i = 0;
        for (; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) return false;
        }
        return i == 12;
    }

    public static boolean checkTask(String task) {
        int cnt = 0;
        for (String i: task.split(", ")) {
            cnt++;
            try {
                Integer.parseInt(i);
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        return cnt > 0;
    }

    public static boolean validateDepartureInput(String[] param) {
        return param.length == DEPARTURE_INPUT_ROWS.getCount() && ValidationService.checkDateFormat(param[0]);
    }

    public static boolean validateGuestInput(String[] param) {
        return param.length == GUEST_INPUT_ROWS.getCount() &&
                ValidationService.checkDateFormat(param[5]) &&
                ValidationService.checkTimeFormat(param[6]) &&
                ValidationService.checkTimeFormat(param[7]);
    }
}
