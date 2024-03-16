package main.core;

import java.util.Date;

public record Passport(
        String passportSeria,
        String passportNumber,
        String passportOrganization,
        Date passportDate,
        String passportDivisionCode
) {
    @Override
    public String toString() {
        return "Паспорт" +
                "\nСерия:" + passportSeria +
                "\nНомер: " + passportNumber +
                "\nКем выдан: " + passportOrganization +
                "\nДата выдачи: " + passportDate +
                "\nКод подразделения: " + passportDivisionCode;
    }
}
