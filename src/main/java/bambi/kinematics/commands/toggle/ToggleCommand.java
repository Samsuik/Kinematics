package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.CommandException;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.player.KinematicsPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ToggleCommand extends KinematicsCommand {
    public ToggleCommand(Kinematics plugin, String name) {
        super(plugin, name);
    }

    public ToggleCommand(Kinematics plugin, String name, List<String> aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        int state = switch (args.length < 1 ? "" : args[0].toLowerCase()) {
            case "toggle", "" -> -1;
            case "0", "off", "false", "disable" -> 0;
            case "enable", "1", "on", "true" -> 1;
            default -> 2;
        };

        if (state != 2) {
            this.toggle(kplayer, state);
            return;
        }

        throw new CommandException("Unable to pars " + args[0] + " to bool");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        throw new CommandException("Player command only");
    }

    public abstract void toggle(KinematicsPlayer kplayer, int number);

    public boolean toggleBool(boolean bool, int i) {
        if (i == -1) {
            return !bool;
        } else {
            return i > 0;
        }
    }
}

