package main.core;

import main.constants.ServicesConstData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Service {
    private String id;
    private String type;
    private ConcurrentHashMap<String, Set<String>> busySlots = new ConcurrentHashMap<>();

    public Service(String id, String type) {
        this.id = id;
        this.type = type;

        fillBusySlots();
    }

    private void fillBusySlots() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        busySlots.put(today.format(formatter), new HashSet<>());
        busySlots.put(today.plusDays(1).format(formatter), new HashSet<>());
        busySlots.put(today.plusDays(2).format(formatter), new HashSet<>());
    }

    public ArrayList<String> getAvailableDate() {
        ArrayList<String> res = new ArrayList<>();

        for (Map.Entry<String, Set<String>> i: busySlots.entrySet()) {
            if (i.getValue().size() != 24) res.add(i.getKey());
        }
        Collections.sort(res);
        return res;
    }


    public ArrayList<String> getAvailableHours(String date) {
        Set<String> busyHours = busySlots.get(date);

        return ServicesConstData.hours.stream().filter(element -> !busyHours.contains(element)).sorted().collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean addHour(String date, String hour) {
        return busySlots.get(date).add(hour);
    }

    @Override
    public String toString() {
        return id + " - " + type;
    }


}
