package bambi.kinematics.player;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.AlertType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class PlayerManager implements Listener {
    // Keep player data around for 2 hours after they log off
    private final Cache<Player, KinematicsPlayer> offlinePlayers = CacheBuilder.newBuilder()
            .maximumSize(600) // limit abuse
            .expireAfterWrite(2, TimeUnit.HOURS) // guava version is before java 8
            .build();

    private final Map<Player, KinematicsPlayer> onlinePlayers = new HashMap<>();
    private final Kinematics kinematics;

    public PlayerManager(Kinematics kinematics) {
        this.kinematics = kinematics;
    }

    public KinematicsPlayer wrap(Player player) {
        return onlinePlayers.computeIfAbsent(player, p -> new KinematicsPlayer(kinematics, p));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        KinematicsPlayer kplayer = offlinePlayers.getIfPresent(event.getPlayer());
        if (kplayer != null) {
            onlinePlayers.put(event.getPlayer(), kplayer);
            offlinePlayers.invalidate(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        KinematicsPlayer kplayer = onlinePlayers.remove(event.getPlayer());
        if (kplayer != null) {
            offlinePlayers.put(event.getPlayer(), kplayer);
        }
    }

    public boolean isPlayerAcceptingAlertNearby(Location location, AlertType type) {
        return onlinePlayers.values().stream()
                .filter(kplayer -> kplayer.isInAlertDistance(location))
                .anyMatch(kplayer -> kplayer.isViewingAlert(type));
    }

    public List<KinematicsPlayer> nearbyPlayers(Location location, Predicate<KinematicsPlayer> predicate) {
        // todo: cache result
        return onlinePlayers.values().stream()
                .filter(kplayer -> kplayer.isInAlertDistance(location))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Collection<KinematicsPlayer> players() {
        return onlinePlayers.values();
    }
}
