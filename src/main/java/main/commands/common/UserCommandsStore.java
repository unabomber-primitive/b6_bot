package main.commands.common;

import java.util.concurrent.ConcurrentHashMap;

public class UserCommandsStore {
    public static ConcurrentHashMap<String, Command> lastUserCommand = new ConcurrentHashMap<>();

}
