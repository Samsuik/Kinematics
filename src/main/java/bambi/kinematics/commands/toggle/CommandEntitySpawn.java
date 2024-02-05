package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandTicksLived;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CommandEntitySpawn extends ToggleCommand {
    public CommandEntitySpawn(Kinematics plugin) {
        super(plugin, "entityspawn", List.of("entityinfo", "info"));
        this.addSub(new CommandTicksLived(plugin));
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.ENTITY_SPAWN, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.ENTITY_SPAWN) ? "" : "no longer ") + "show entity spawning"));
    }
}

