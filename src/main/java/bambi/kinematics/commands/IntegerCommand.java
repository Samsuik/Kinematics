package bambi.kinematics.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class IntegerCommand extends KinematicsCommand {
    public IntegerCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            this.execute(sender, IntegerCommand.parsInt(args[0]));
        }
        this.sendMessage(sender);
    }

    @Override
    public void execute(Player p, String[] args) throws CommandException {
        this.execute((CommandSender) p, args);
    }

    public abstract void execute(CommandSender var1, int var2);

    public abstract void sendMessage(CommandSender var1);
}

