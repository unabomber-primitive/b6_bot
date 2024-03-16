package main.constants;

import java.util.ArrayList;
import static main.constants.ButtonNames.*;

public class StartStudentButtonsName {
    private static final ArrayList<String> buttonsNames = new ArrayList<>();

    static {
        buttonsNames.add(SERVICE_CHOOSE.getName());
        buttonsNames.add(BOOKING.getName());
        buttonsNames.add(GUEST_PASS.getName());
        buttonsNames.add(APPLICATIONS.getName());
        buttonsNames.add(DEPARTURE_REGISTRATION.getName());
    }

    public static ArrayList<String> getButtonsNames() {
        return buttonsNames;
    }
}
