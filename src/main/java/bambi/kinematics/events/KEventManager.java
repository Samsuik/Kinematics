package bambi.kinematics.events;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.TemplatedMessage;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static bambi.kinematics.utils.TemplatedMessage.format;
import static bambi.kinematics.utils.TemplatedMessage.simplePlaceholder;

public final class KEventManager implements Listener, Runnable {
    private static final Vector ENTITY_CENTRE = new Vector(0.0f, 0.49f, 0.0f);

    private final Kinematics kinematics;
    private final Map<KinematicsPlayer, List<Kevent<?>>> viewedEvents = new HashMap<>();
    private int tick;

    public KEventManager(Kinematics kinematics) {
        this.kinematics = kinematics;
    }

    public Set<Vector> fetchNearbyEntities(EntityExplodeEvent event) {
        return event.getEntity().getNearbyEntities(8.0, 8.0, 8.0).stream()
                .filter(entity -> entity.getType() == EntityType.PRIMED_TNT || entity.getType() == EntityType.FALLING_BLOCK)
                .map(entity -> entity.getLocation().toVector().add(ENTITY_CENTRE))
                .collect(Collectors.toSet());
    }

    public void schedule(EntityExplodeEvent e) {
        List<KinematicsPlayer> nearbyPlayers = kinematics.getPlayerManager().nearbyPlayers(e.getEntity().getLocation(),
                kplayer -> kplayer.isViewingAlert(AlertType.EXPLOSION));

        Set<Vector> nearbyEntities = null;

        for (KinematicsPlayer kplayer : nearbyPlayers) {
            int order = kplayer.getProperties().getOrder();
            Kexplosion kexplosion = new Kexplosion(kplayer, e, tick, order);

            // keep nearby entities across events
            if (kplayer.isViewingAlert(AlertType.GET_NEARBY)) {
                if (nearbyEntities == null) {
                    nearbyEntities = fetchNearbyEntities(e);
                }

                kexplosion.setNearbyEntities(nearbyEntities);
            }

            schedule(kplayer, kexplosion);
            kplayer.getProperties().setOrder(order + 1);
        }
    }

    public void schedule(EntityChangeBlockEvent e) {
        List<KinematicsPlayer> nearbyPlayers = kinematics.getPlayerManager().nearbyPlayers(e.getEntity().getLocation(),
                kplayer -> kplayer.isViewingAlert(AlertType.STACK));

        for (KinematicsPlayer kplayer : nearbyPlayers) {
            int order = kplayer.getProperties().getOrder();
            schedule(kplayer, new Kstack(kplayer, e, tick, order));
            kplayer.getProperties().setOrder(order + 1);
        }
    }

    private void schedule(KinematicsPlayer kplayer, Kevent<?> kevent) {
        List<Kevent<?>> events = viewedEvents.computeIfAbsent(kplayer, k -> new ArrayList<>());

        if (events.isEmpty() || !events.get(events.size() - 1).add(kevent)) {
            events.add(kevent);
        }
    }

    @Override
    public void run() {
        viewedEvents.forEach((kplayer, events) -> {
            for (Kevent<?> e : events) {
                if (!e.isIgnored(kplayer) && kplayer.isViewingAlert(e.getType())) {
                    kplayer.sendTemplatedMessage(e.template(kplayer));
                }

                if (!(e instanceof Kexplosion exp) || exp.isSwinging() || !kplayer.isViewingAlert(AlertType.GET_NEARBY) || exp.getNearbyEntities().size() < kplayer.getProperties().getMinNearby()) {
                    continue;
                }

                for (Vector vec : exp.getNearbyEntities()) {
                    vec.subtract(exp.getPosition());
                    double d = vec.length();
                    if (d < 0.0 || d > 8.0) continue;
                    Vector rel = vec.clone().multiply((-0.125 * d + 1.0) / d);

                    kplayer.sendTemplatedMessage(new TemplatedMessage(kinematics.getConfiguration().getnearbytemplate, vec, rel, tick, -1, -1, "", NamedTextColor.WHITE)
                            .extraPlaceholder(simplePlaceholder("@d", format(d, kplayer)))
                            .extraPlaceholder(simplePlaceholder("@vd", "(%s)".formatted(format(1.0 - 0.125 * d, kplayer))))
                    );
                }
            }

            kplayer.getProperties().setOrder(0);
        });

        viewedEvents.clear();
        tick++;
    }

}
