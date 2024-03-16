package main.core;

import java.util.Date;

public record Application(
        Guest guest,
        Integer cardNumber,
        String startTime,
        String endTime,
        String arrivingDate
) {
    @Override
    public String toString() {
        return guest +
                "\nКто пригласил: " + cardNumber +
                "\nДата: " + arrivingDate +
                "\nВремя прибытия: " + startTime +
                "\nВремя убытия: " + endTime;

    }
}