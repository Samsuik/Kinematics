package bambi.kinematics.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public abstract class ToggleCommand extends KinematicsCommand {
    public ToggleCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Player p, String[] args) throws CommandException {
        switch (args.length < 1 ? "" : args[0].toLowerCase()) {
            case "toggle":
            case "": {
                this.toggle(p, -1);
                return;
            }
            case "0":
            case "off":
            case "false":
            case "disable": {
                this.toggle(p, 0);
                return;
            }
            case "enable":
            case "1":
            case "on":
            case "true": {
                this.toggle(p, 1);
                return;
            }
        }

        throw new CommandException("Unable to pars " + args[0] + " to bool");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        throw new CommandException("Player command only");
    }

    public abstract void toggle(Player var1, int var2);

    public HashSet<Player> toggleSet(HashSet<Player> set, Player p, int i) {
        switch (i) {
            case -1 -> {
                if (set.remove(p)) break;
                set.add(p);
            }
            case 0 -> set.remove(p);
            case 1 -> set.add(p);
        }

        return set;
    }

    public boolean toggleBool(boolean bool, int i) {
        if (i == -1) {
            return !bool;
        } else {
            return i > 0;
        }
    }
}

