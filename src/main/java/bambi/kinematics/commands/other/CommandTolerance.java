package bambi.kinematics.commands.other;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.CommandException;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.enums.Direction;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static bambi.kinematics.utils.TemplatedMessage.format;

public final class CommandTolerance extends KinematicsCommand {
    public CommandTolerance(Kinematics plugin) {
        super(plugin, "tolerance", List.of("tol"));
    }

    @Override
    public void baseCommand(@NotNull CommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof Player player)) {
            throw new CommandException("Player command only");
        }

        KinematicsPlayer kplayer = getPlugin().getPlayerManager().wrap(player);

        if (args.length >= 3) {
            this.setEachDirection(kplayer, args);
        } else if (args.length > 0) {
            // try to find a direction ex: [2x, 2z] [4, x] [0y]
            Direction carriedDirection = null;
            boolean noMatches = true; // ?
            for (int i = args.length - 1; i >= 0; --i) {
                String arg = args[i];
                Optional<Direction> match = Arrays.stream(Direction.values())
                        .filter(dir -> arg.endsWith(dir.lowerCaseName()))
                        .findAny();
                if (match.isEmpty() && carriedDirection == null)
                    continue; // was unable to find a match

                Direction direction = match.orElse(carriedDirection);
                String cleanArg = arg.replace(direction.lowerCaseName(), "");

                if (cleanArg.isEmpty()) {
                    carriedDirection = direction;
                } else {
                    noMatches = false;
                    this.setDirection(kplayer, cleanArg, direction);
                }
            }

            if (noMatches) {
                this.setEachDirection(kplayer, new String[]{args[0], args[0], args[0]});
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
        setDirection(kplayer, args[0], Direction.X);
        setDirection(kplayer, args[1], Direction.Y);
        setDirection(kplayer, args[2], Direction.Z);
    }

    private void setDirection(KinematicsPlayer kplayer, String arg, Direction dir) throws CommandException {
        kplayer.getProperties().setExplosionTolerance(dir.setVec(kplayer.getProperties().getExplosionTolerance(), CommandTolerance.parsDouble(arg)));
    }
}

