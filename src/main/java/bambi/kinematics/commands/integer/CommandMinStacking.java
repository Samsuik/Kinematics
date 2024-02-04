package bambi.kinematics.commands.integer;

import bambi.kinematics.Kinematics;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandMinStacking extends IntegerCommand {
    public CommandMinStacking(Kinematics plugin) {
        super(plugin, "minstacking", List.of("min", "minimum"));
    }

    @Override
    public void execute(KinematicsPlayer kplayer, int i) {
        kplayer.getProperties().setMinStacking(Math.max(i, 0));
    }

    @Override
    public void sendMessage(KinematicsPlayer kplayer) {
        kplayer.sendPrefixedMessage(Component.text("will show " + kplayer.getProperties().getMinStacking() + " stacking or more"));
    }
}

