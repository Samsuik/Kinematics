package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandMinNearby;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandShowEntityFeet extends ToggleCommand {
    public CommandShowEntityFeet(Kinematics plugin) {
        super(plugin, "showfeet", List.of("base", "showbase", "feet"));
        this.addSub(new CommandMinNearby(plugin));
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.ENTITY_FEET, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.ENTITY_FEET) ? "" : "no longer ") + "show entity feet"));
    }
}

