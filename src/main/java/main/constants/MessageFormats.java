package main.constants;

public enum MessageFormats {

    COMMON("Введите в следующем формате:\n"),

    GUEST_INFO("""
            ФИО
            Серия и номер паспорта
            Кем выдан
            Когда
            Код подразделения
            Дата посещения
            Время прибытия
            Время убытия
            
            
            ОБРАЗЕЦ:
            Иванов Иван Иванович
            1234 567890
            МВД
            07.06.2003
            130-006
            2024-08-31
            09:00:00
            19:00:00
    """),

    DATA_INFO("Введите дату отъезда в формате yyyy-mm-dd"),
    NEWS_INFO("Заголовок\nСодержание новости"),
    COMPLETED_TASK_INFO("""
            Номер телефона сотрудника
            ID выполненных задач через запятую
            
            Пример:
            +79221334545
            5, 7, 10, 20
            
            +79865321212
            1, 2, 9
    """);

    private String format;


    MessageFormats(String s) {
        format = s;
    }

    public String getFormat() {
        return format;
    }
}
