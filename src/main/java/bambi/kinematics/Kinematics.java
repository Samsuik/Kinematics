package bambi.kinematics;

import bambi.kinematics.commands.CommandKinematics;
import bambi.kinematics.commands.CommandKspawn;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.commands.toggle.CommandProtection;
import bambi.kinematics.configuration.Configuration;
import bambi.kinematics.events.KEventManager;
import bambi.kinematics.listeners.EntitySpawnListener;
import bambi.kinematics.listeners.KinematicsListener;
import bambi.kinematics.player.PlayerManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Kinematics extends JavaPlugin {
    private KEventManager eventManager;
    private PlayerManager playerManager;
    private Configuration configuration;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // register config
        this.configuration = new Configuration(this);

        // adventure
        this.adventure = BukkitAudiences.create(this);

        // register commands
        this.initializeCommands();

        // managers
        this.eventManager = new KEventManager(this);
        this.playerManager = new PlayerManager(this);

        // register listeners
        EntitySpawnListener spawnListener = new EntitySpawnListener(this);
        this.getServer().getPluginManager().registerEvents(new KinematicsListener(this), this);
        this.getServer().getPluginManager().registerEvents(spawnListener, this);
        this.getServer().getPluginManager().registerEvents(playerManager, this);
        this.getServer().getPluginManager().registerEvents(eventManager, this);

        // any tasks to schedule?
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, eventManager, 0L, 1L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, spawnListener, 0L, 1L);
    }

    private void initializeCommands() {
        List<KinematicsCommand> commands = new ArrayList<>();
        commands.add(new CommandKinematics(this));
        commands.add(new CommandKspawn(this));
        commands.add(new CommandProtection(this));

        // register commands
        for (KinematicsCommand kcommand : commands) {
            this.getCommand(kcommand.getName()).setExecutor(kcommand);
            this.getCommand(kcommand.getName()).setTabCompleter(kcommand);
        }
    }

    public final KEventManager getEventManager() {
        return eventManager;
    }

    public final PlayerManager getPlayerManager() {
        return playerManager;
    }

    public final Configuration getConfiguration() {
        return configuration;
    }

    public final BukkitAudiences getAdventure() {
        return adventure;
    }
}

