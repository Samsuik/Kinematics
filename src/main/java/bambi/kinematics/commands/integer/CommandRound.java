package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandRound extends IntegerCommand {
    public CommandRound(Kinematics plugin) {
        super(plugin, "round", List.of("r", "decimals"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setDecimals(Math.max(i, -1));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        if (kplayer.getProperties().getDecimals() == -1) {
            kplayer.sendPrefixedMessage(Component.text("formatting is disabled"));
        } else {
            kplayer.sendPrefixedMessage(Component.text("will round to " + kplayer.getProperties().getDecimals() + " decimals"));
        }
    }
}

