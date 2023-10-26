package bambi.kinematics.commands;

import org.bukkit.ChatColor;

public class CommandException extends Exception {
    private static final long serialVersionUID = -4797298126884473883L;
    private static final String prefix = ChatColor.DARK_RED + "Kinematics syntax error: " + ChatColor.GRAY;

    public CommandException(String message) {
        super(prefix + message);
    }
}

