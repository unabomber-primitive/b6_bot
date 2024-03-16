package main.constants;

public enum ButtonNames {
    START("/start"),
    GUEST_PASS("Гостевой пропуск"),
    SERVICE_CHOOSE("Выбор сервиса"),
    BOOKING("Бронь комнаты"),
    APPLICATIONS("Мои заявки"),
    DEPARTURE_REGISTRATION("Оформление выезда"),
    ADD_NEWS("Добавить новость"),
    APPROVE_GUEST("Подтвердить гостей"),
    COMPLETED_TASKS("Завершенные задачи"),
    WHO_HAS_LEFT("Просмотреть выехавших"),
    NEW_TASKS("Новые задачи"),
    EMPLOYEE("Сотрудник"),
    STUDENT("Студент");

    private String name;

    ButtonNames(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}
