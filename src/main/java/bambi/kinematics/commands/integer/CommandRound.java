package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandRound extends IntegerCommand {
    public CommandRound(Kinematics plugin) {
        super(plugin, "round", List.of("decimals"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setDecimals(Math.max(i, 0));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        kplayer.sendPrefixedMessage(Component.text("will round to " + kplayer.getProperties().getDecimals() + " decimals"));
    }
}

