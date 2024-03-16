package main.constants;

import java.util.ArrayList;

import static main.constants.ButtonNames.EMPLOYEE;
import static main.constants.ButtonNames.STUDENT;

public class EmployeeOrStudentButtons {

    private static final ArrayList<String> buttonsNames = new ArrayList<>();

    static {
        buttonsNames.add(EMPLOYEE.getName());
        buttonsNames.add(STUDENT.getName());
    }

    public static ArrayList<String> getButtonsNames() {
        return buttonsNames;
    }
}
