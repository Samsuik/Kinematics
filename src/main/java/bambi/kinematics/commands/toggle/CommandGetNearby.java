package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandMinNearby;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandGetNearby extends ToggleCommand {
    public CommandGetNearby(Kinematics plugin) {
        super(plugin, "getnearby", List.of("nearby", "near", "efficiency", "effi"));
        this.addSub(new CommandMinNearby(plugin));
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.GET_NEARBY, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.GET_NEARBY) ? "" : "no longer ") + "show entities near explosions"));
    }
}

