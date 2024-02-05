package bambi.kinematics.utils;

import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public final class TemplatedMessage {
    private Component base;
    private final Vector pos, vel;
    private final int tick, order, amount;
    private final String type;
    private final TextColor colour;

    public TemplatedMessage(Component base, Vector pos, Vector vel, int tick, int order, int amount, String type, TextColor colour) {
        this.base = base;
        this.pos = pos;
        this.vel = vel;
        this.tick = tick;
        this.order = order;
        this.amount = amount;
        this.type = type;
        this.colour = colour;
    }

    public TemplatedMessage extraPlaceholder(Consumer<TextReplacementConfig.Builder> placeholder) {
        base = base.replaceText(placeholder);
        return this;
    }

    public Component componentFor(KinematicsPlayer kplayer) {
        // AAAAAAAAAAAAAAAAAaa template hell, get out of my head
        return base.replaceText(simplePlaceholder("@tick", tick))
                .replaceText(conditionalPlaceholder("@order", order, order >= 0))
                .replaceText(conditionalPlaceholder("@amount", amount, amount >= 0))
                .replaceText(simplePlaceholder("@type", type))
                .replaceText(simplePlaceholder("@x", format(pos.getX(), kplayer)))
                .replaceText(simplePlaceholder("@y", format(pos.getY(), kplayer)))
                .replaceText(simplePlaceholder("@z", format(pos.getZ(), kplayer)))
                .replaceText(velocityPlaceholder("@vx", vel.getX(), kplayer))
                .replaceText(velocityPlaceholder("@vy", vel.getY(), kplayer))
                .replaceText(velocityPlaceholder("@vz", vel.getZ(), kplayer))
                .color(colour)
                .clickEvent(ClickEvent.runCommand(createTeleportCommand(kplayer, pos)));
    }

    private String createTeleportCommand(KinematicsPlayer kplayer, Vector pos) {
        if (kplayer.getPlayer().hasPermission("minecraft.command.teleport")) {
            return "/tp " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
        } else {
            return "";
        }
    }

    public static Consumer<TextReplacementConfig.Builder> velocityPlaceholder(String placeholder, double with, KinematicsPlayer kplayer) {
        double formattedVelocity = format(with, kplayer);
        boolean condition = kplayer.isViewingAlert(AlertType.SHOW_ENTITY_VELOCITY);
        return conditionalPlaceholder(placeholder, (with == 0.0) ? "" : "(%s)".formatted(formattedVelocity), condition);
    }

    public static Consumer<TextReplacementConfig.Builder> simplePlaceholder(String placeholder, Object with) {
        return b -> b.matchLiteral(placeholder).replacement(with.toString()).once();
    }

    public static Consumer<TextReplacementConfig.Builder> conditionalPlaceholder(String placeholder, Object with, boolean condition) {
        return b -> b.matchLiteral(placeholder).replacement(!condition ? "" : with.toString()).once();
    }

    public static double format(double n, KinematicsPlayer kplayer) {
        return format(n, kplayer.getProperties().getDecimals());
    }

    public static double format(double n, int d) {
        double pow = Math.pow(10, d);
        return Math.round(n * pow) / pow;
    }
}
