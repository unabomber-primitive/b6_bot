package main.core;

import java.util.Date;
public record Task(
        Long serviceId,
        String problemDescription,
        Date chosenTime,
        Date creationTime
) {
    @Override
    public String toString() {
        return "serviceId = " + serviceId +
                "\nОписание проблемы: " + problemDescription +
                "\nВыбранное время и дата: " + chosenTime +
                "\nДата создания заявки: " + creationTime;
    }
}
