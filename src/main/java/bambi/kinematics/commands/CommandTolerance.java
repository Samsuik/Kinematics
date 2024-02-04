package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.Direction;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static bambi.kinematics.utils.TemplatedMessage.format;

public class CommandTolerance extends KinematicsCommand {
    public CommandTolerance(Kinematics plugin) {
        super(plugin, "tolerance", List.of("tol"));
    }

    @Override
    public void baseCommand(@NotNull CommandSender sender, @NotNull String fullCommand, String[] args) throws CommandException {
        if (!(sender instanceof Player player)) {
            throw new CommandException("Player command only");
        }

        KinematicsPlayer kplayer = getPlugin().getPlayerManager().wrap(player);

        if (args.length >= 3) {
            this.setEachDirection(kplayer, args);
        } else if (args.length == 2) {
            for (Direction dir : Direction.values()) {
                if (args[0].equals(dir.name())) {
                    this.setDirection(kplayer, args[1], dir);
                }
            }
        } else if (args.length == 1) {
            for (Direction dir : Direction.values()) {
                if (fullCommand.endsWith(dir.name())) {
                    this.setDirection(kplayer, args[0], dir);
                }
            }
        }

        Vector tolerance = kplayer.getProperties().getExplosionTolerance();
        kplayer.sendPrefixedMessage(Component.text("explosion tolerance: x: %s y: %s z: %s".formatted(
                format(tolerance.getX(), 2), format(tolerance.getY(), 2), format(tolerance.getZ(), 2)
        )));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {}

    private void setEachDirection(KinematicsPlayer kplayer, String[] args) throws CommandException {
        setDirection(kplayer, args[0], Direction.x);
        setDirection(kplayer, args[1], Direction.y);
        setDirection(kplayer, args[2], Direction.z);
    }

    private void setDirection(KinematicsPlayer kplayer, String arg, Direction dir) throws CommandException {
        kplayer.getProperties().setExplosionTolerance(dir.setvec(kplayer.getProperties().getExplosionTolerance(), CommandTolerance.parsDouble(arg)));
    }
}

