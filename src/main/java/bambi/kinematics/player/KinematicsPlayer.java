package bambi.kinematics.player;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;

public final class KinematicsPlayer {
    private final Kinematics kinematics;
    private final Player player;
    private final Set<AlertType> displayAlerts = EnumSet.noneOf(AlertType.class);
    private final Properties properties = new Properties(); // DISGUSTING.

    public KinematicsPlayer(Kinematics kinematics, Player player) {
        this.kinematics = kinematics;
        this.player = player;
        this.properties.setDistance(Math.min(kinematics.getConfiguration().maxAlertDistance/2, player.getViewDistance()*16));
        this.displayAlerts.add(AlertType.SHOW_ENTITY_VELOCITY);
        this.displayAlerts.add(AlertType.SHOW_ON_GROUND);
    }

    public Kinematics getKinematics() {
        return kinematics;
    }

    public Player getPlayer() {
        return player;
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean isViewingAlert(AlertType type) {
        return displayAlerts.contains(type);
    }

    public void toggleAlert(AlertType type, int i) {
        switch (i) {
            case -1 -> toggleAlert(type);
            case 0 -> displayAlerts.remove(type);
            case 1 -> displayAlerts.add(type);
        }
    }

    public void toggleAlert(AlertType type) {
        if (!displayAlerts.remove(type)) {
            displayAlerts.add(type);
        }
    }

    public boolean isInAlertDistance(Location location) {
        // actually stupid that these world checks are required
        return location.getWorld() != null && player.getWorld().equals(location.getWorld())
            && player.getLocation().distanceSquared(location) < properties.getDistance();
    }

    public void sendPrefixedMessage(Component component) {
        player.sendMessage(kinematics.getConfiguration().prefix.append(component));
    }

    public void sendTemplatedMessage(TemplatedMessage template) {
        player.sendMessage(template.componentFor(this));
    }
}
