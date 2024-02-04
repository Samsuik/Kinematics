package bambi.kinematics.commands;

public class CommandException extends Exception {
    private static final String EXCEPTION_PREFIX = "<dark_red>Kinematics syntax error: </dark_red><gray>";

    public CommandException(String message) {
        super(EXCEPTION_PREFIX + message);
    }
}

