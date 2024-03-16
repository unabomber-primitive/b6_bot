package main.utils;

import main.commands.common.Command;
import main.commands.common.Start;
import main.commands.employee.*;
import main.commands.student.*;
import static main.constants.ButtonNames.*;

import java.util.HashMap;

public class CommandFabric {
    private final HashMap<String, CreateObject> commands = new HashMap<>();

    public CommandFabric() {
        commands.put(START.getName(), this::getStart);
        commands.put(GUEST_PASS.getName(), this::getGuestInvitation);
        commands.put(APPLICATIONS.getName(), this::getApplications);
        commands.put(DEPARTURE_REGISTRATION.getName(), this::getDepartureRegistration);
        commands.put(BOOKING.getName(), this::getBooking);
        commands.put(SERVICE_CHOOSE.getName(), this::getServiceChoose);
        commands.put(WHO_HAS_LEFT.getName(), this::getWhoHaveLeft);
        commands.put(NEW_TASKS.getName(), this::getNewTasks);
        commands.put(APPROVE_GUEST.getName(), this::getApproveGuest);
        commands.put(ADD_NEWS.getName(), this::getAddNews);
        commands.put(COMPLETED_TASKS.getName(), this::getCompletedTask);
    }

    public Command getCommand(String command) {
        if (!commands.containsKey(command)) return null;
        return commands.get(command).getCommand();
    }

    private ServiceChoose getServiceChoose() {
        return new ServiceChoose();
    }

    private BookingCommand getBooking() {
        return new BookingCommand();
    }

    private ApplicationsCheck getApplications() {
        return new ApplicationsCheck();
    }

    private DepartureRegistration getDepartureRegistration() {
        return new DepartureRegistration();
    }

    private GuestInvitation getGuestInvitation() {
        return new GuestInvitation();
    }

    private Start getStart() {
        return new Start();
    }

    private WhoHasLeftCheck getWhoHaveLeft() {
        return new WhoHasLeftCheck();
    }

    private NewTasksCheckCommand getNewTasks() {
        return new NewTasksCheckCommand();
    }

    private ApproveGuestCommand getApproveGuest() {
        return new ApproveGuestCommand();
    }

    private AddNewsCommand getAddNews() {
        return new AddNewsCommand();
    }

    private CompletedTaskCommand getCompletedTask() {
        return new CompletedTaskCommand();
    }
}
