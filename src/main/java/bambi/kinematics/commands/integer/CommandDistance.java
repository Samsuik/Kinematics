package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandDistance extends IntegerCommand {
    public CommandDistance(Kinematics plugin) {
        super(plugin, "distance", List.of("dist"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setDistance(Math.max(Math.min(i, kplayer.getKinematics().getConfiguration().maxAlertDistance), 0));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        kplayer.sendPrefixedMessage(Component.text("will show alerts up to " + kplayer.getProperties().getDistance() + " blocks"));
    }
}

