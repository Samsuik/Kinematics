package bambi.kinematics.commands.toggle;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.integer.CommandMinStacking;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandStacking extends ToggleCommand {
    public CommandStacking(Kinematics plugin) {
        super(plugin, "stacking", List.of("stacks", "stack"));
        this.addSub(new CommandMinStacking(plugin));
    }

    @Override
    public @Nullable String description() {
        return "falling block stack alerts";
    }

    @Override
    public void toggle(KinematicsPlayer kplayer, int i) {
        kplayer.toggleAlert(AlertType.STACK, i);
        kplayer.sendPrefixedMessage(Component.text("will " + (kplayer.isViewingAlert(AlertType.STACK) ? "" : "no longer ") + "show stacking"));
    }
}

