package main.constants;

public enum StateInfo {
    NO_DATA("Ничего нет"),
    SUCCESS("Запрос успешно обработан"),
    GUEST_APPROVED("Ваше заявление на провод гостя одобрено!");

    private String state;

    StateInfo(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
