package bambi.kinematics.commands.other;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.CommandException;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.player.KinematicsPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class CommandWait extends KinematicsCommand {
    public CommandWait(Kinematics plugin) {
        super(plugin, "wait", List.of("w"));
    }

    @Override
    public boolean showInHelpMessage() {
        return false;
    }

    @Override
    public boolean isTabCompletable() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        throw new CommandException("can only be executed as a chain command");
    }

    @Override
    public List<String> tabComplete(KinematicsPlayer kplayer, String fullCommand, String[] args) {
        return args.length == 1 ? listOfNumbers() : List.of();
    }
}
