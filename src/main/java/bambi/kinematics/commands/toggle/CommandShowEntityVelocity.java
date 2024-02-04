package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandShowEntityVelocity extends ToggleCommand {
    public CommandShowEntityVelocity(Kinematics plugin) {
        super(plugin, "showentityvelocity", List.of("velocity", "vel"));
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
//        kplayer.getProperties().setShowEntityVelocity(toggleBool(kplayer.getProperties().isShowEntityVelocity(), i));
        kplayer.toggleAlert(AlertType.SHOW_ENTITY_VELOCITY, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.SHOW_ENTITY_VELOCITY) ? "" : "no longer ") + "show entity velocity"));
    }
}

