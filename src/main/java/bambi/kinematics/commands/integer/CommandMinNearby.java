package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandMinNearby extends IntegerCommand {
    public CommandMinNearby(Kinematics plugin) {
        super(plugin, "minnearby", List.of("min", "minimum"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setMinNearby(Math.max(i, 0));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        kplayer.sendPrefixedMessage(Component.text("will show nearby entities at" + kplayer.getProperties().getMinNearby() + " explosions or more"));
    }
}

