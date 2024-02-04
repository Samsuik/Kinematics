package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.CommandException;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.player.KinematicsPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class IntegerCommand extends KinematicsCommand {
    public IntegerCommand(Kinematics plugin, String name) {
        super(plugin, name);
    }

    public IntegerCommand(Kinematics plugin, String name, List<String> aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public final void execute(CommandSender sender, String[] args) throws CommandException {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        if (args.length > 0) {
            this.execute(kplayer, IntegerCommand.parsInt(args[0]));
        }
        this.sendMessage(kplayer);
    }

    public abstract void execute(KinematicsPlayer kplayer, int number);

    public abstract void sendMessage(KinematicsPlayer kplayer);
}

