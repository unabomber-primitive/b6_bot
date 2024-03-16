package main.constants;

import main.core.Service;
import main.database.DatabaseManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ServicesConstData {
    public static final HashMap<String, Service> services = DatabaseManager.getServices();
    public static final Set<String> hours = new HashSet<>();

    private static final int START_WORK = 9;
    private static final int END_WORK = 17;

    static {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (int i = START_WORK; i < END_WORK; i++) {
            for (int j = 0; j < 60; j += 30) {
                hours.add(LocalTime.of(i, j).format(formatter));
            }
        }
    }
}
