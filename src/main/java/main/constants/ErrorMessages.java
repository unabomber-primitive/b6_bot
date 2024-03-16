package main.constants;

public enum ErrorMessages {
    INPUT_ERROR("Ошибка в формате параметров"),
    PERMISSION_ERROR("Недостаточно прав для выполнения этой команды"),
    DATABASE_ERROR("Ошибка на стороне базы данных, попробуйте позже"),
    UNKNOWN_COMMAND("Неизвестная команда"),
    BLOCKED_CARD("Пропуск заблокирован.");

    private String error;


    ErrorMessages(String s) {
        error = s;
    }

    public String getError() {
        return error;
    }
}
