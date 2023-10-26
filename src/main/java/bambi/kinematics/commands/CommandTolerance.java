package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.Util;
import bambi.kinematics.enums.Direction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTolerance extends KinematicsCommand {
    public CommandTolerance() {
        super("tolerance");
        this.addAlias("tol");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length >= 3) {
            this.execute(sender, args);
        } else if (args.length >= 2) {
            for (Direction dir : Direction.values()) {
                if (args[0].equals(dir.name())) {
                    this.execute(args[1], dir);
                }
            }
        } else if (args.length >= 1) {
            for (Direction dir : Direction.values()) {
                if (label.endsWith(dir.name())) {
                    this.execute(args[0], dir);
                }
            }
        }

        sender.sendMessage(CommandTolerance.getPrefix() + "explosion tolerance: " + Util.vectoString(Kinematics.getexplosionTolerance(), 2));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        this.execute(args[0], Direction.x);
        this.execute(args[1], Direction.y);
        this.execute(args[2], Direction.z);
    }

    @Override
    public void execute(Player p, String[] args) throws CommandException {
        this.execute((CommandSender) p, args);
    }

    public void execute(String arg, Direction dir) throws CommandException {
        Kinematics.setexplosionTolerance(dir.setvec(Kinematics.getexplosionTolerance(), CommandTolerance.parsDouble(arg)));
    }

    @Override
    public void addAlias(String ali) {
        super.addAlias(ali);
        super.addAlias(ali + "x");
        super.addAlias(ali + "y");
        super.addAlias(ali + "z");
    }
}

