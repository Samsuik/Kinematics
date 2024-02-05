package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandTicksLived extends IntegerCommand {
    public CommandTicksLived(Kinematics plugin) {
        super(plugin, "tickslived", List.of("tl", "ticks"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setTicksLived(Math.min(Math.max(i, 0), 80));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        kplayer.sendPrefixedMessage(Component.text("will show entities after " + kplayer.getProperties().getTicksLived() + " ticks lived"));
    }
}

