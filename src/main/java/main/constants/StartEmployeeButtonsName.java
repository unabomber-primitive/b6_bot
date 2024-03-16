package main.constants;

import java.util.ArrayList;
import static main.constants.ButtonNames.*;

public class StartEmployeeButtonsName {
    private static final ArrayList<String> buttonsNames = new ArrayList<>();

    static {
        buttonsNames.add(ADD_NEWS.getName());
        buttonsNames.add(APPROVE_GUEST.getName());
        buttonsNames.add(NEW_TASKS.getName());
        buttonsNames.add(COMPLETED_TASKS.getName());
        buttonsNames.add(WHO_HAS_LEFT.getName());
    }

    public static ArrayList<String> getButtonsNames()  {
        return buttonsNames;
    }
}
