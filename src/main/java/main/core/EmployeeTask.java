package main.core;

import java.util.Date;

public record EmployeeTask(
        Integer roomNumber,
        Integer cardNumber,
        String problemDescription,
        Date chosenTime
) {
    @Override
    public String toString() {
        return "Номер комнаты: " + roomNumber +
                "\nВыбранное время: " + chosenTime +
                "\nОписание проблемы: " + problemDescription +
                "\nНомер карты студента: " + cardNumber;
    }
}
