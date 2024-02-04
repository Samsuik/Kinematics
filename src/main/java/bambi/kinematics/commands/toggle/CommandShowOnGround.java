package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandShowOnGround extends ToggleCommand {
    public CommandShowOnGround(Kinematics plugin) {
        super(plugin, "showonground", List.of("onground", "ground"));
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.SHOW_ON_GROUND, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.SHOW_ON_GROUND) ? "" : "no longer ") + "show explosions on the ground"));
    }
}

