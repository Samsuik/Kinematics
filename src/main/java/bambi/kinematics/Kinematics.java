package bambi.kinematics;

import bambi.kinematics.commands.CommandKinematics;
import bambi.kinematics.commands.CommandKspawn;
import bambi.kinematics.commands.CommandProtection;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.events.ChangeBlockEvent;
import bambi.kinematics.events.ExplodeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Kinematics extends JavaPlugin {
    private static int tick = 0;
    private static int order = 0;
    private static HashSet<Player> explosionPlayerSet = new HashSet<>();
    private static HashSet<Player> stackPlayerSet = new HashSet<>();
    private static HashSet<Player> getnearbyPlayerSet = new HashSet<>();
    private static HashSet<Player> entityspawnPlayerSet = new HashSet<>();
    private static Vector explosionTolerance = new Vector(0.0, 2.0, 0.0);
    private static boolean showExplosionsonGround = true;
    private static int minExplosions = 0;
    private static int minStacking = 0;
    private static int minNearby = 100;
    private static int decimals = 4;
    private static String explosionstemplate;
    private static String stackingtemplate;
    private static String getnearybtemplate;
    private static String entityspawntempalte;
    private static boolean protection;
    private static HashSet<Material> notProtected;
    private static final ArrayList<Kevent> events;
    private static final HashMap<String, KinematicsCommand> commands;

    static {
        protection = true;
        notProtected = new HashSet<>();
        events = new ArrayList<>();
        commands = new HashMap<>();
    }

    public static void add(EntityExplodeEvent e) {
        if (events.isEmpty() || !events.get(events.size() - 1).add(e)) {
            events.add(new Kexplosion(e, tick, order));
        }
        ++order;
    }

    public static void add(EntityChangeBlockEvent e) {
        if (events.isEmpty() || !events.get(events.size() - 1).add(e)) {
            events.add(new Kstack(e, tick, order));
        }
        ++order;
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ExplodeEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChangeBlockEvent(), this);
        this.initializeCommands();
        this.config();
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickLoop(), 0L, 1L);
    }

    @Override
    public void onDisable() {
    }

    private void config() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        explosionstemplate = this.getConfig().getString("templates.explosions");
        stackingtemplate = this.getConfig().getString("templates.stacking");
        getnearybtemplate = this.getConfig().getString("templates.nearby");
        entityspawntempalte = this.getConfig().getString("templates.entityspawn");
        protection = this.getConfig().getBoolean("protection.enable");

        for (String s : this.getConfig().getStringList("protection.not_protected")) {
            try {
                notProtected.add(Material.valueOf(s.toUpperCase()));
            } catch (Exception e) {
                System.out.println("[Kinematics] Error trying to load material: " + s);
            }
        }
    }

    private void initializeCommands() {
        commands.put("kinematics", new CommandKinematics());
        commands.put("kspawn", new CommandKspawn());
        commands.put("protection", new CommandProtection());
        for (Map.Entry<String, KinematicsCommand> entry : commands.entrySet()) {
            this.getCommand(entry.getKey()).setExecutor(entry.getValue());
            this.getCommand(entry.getKey()).setTabCompleter(entry.getValue());
        }
    }

    public static int getTick() {
        return tick;
    }

    public static HashSet<Player> getExplosionPlayerSet() {
        return explosionPlayerSet;
    }

    public static void setExplosionPlayerSet(HashSet<Player> explosionPlayerSet) {
        Kinematics.explosionPlayerSet = explosionPlayerSet;
    }

    public static HashSet<Player> getStackPlayerSet() {
        return stackPlayerSet;
    }

    public static void setStackPlayerSet(HashSet<Player> stackPlayerSet) {
        Kinematics.stackPlayerSet = stackPlayerSet;
    }

    public static HashSet<Player> getGetnearbyPlayer() {
        return getnearbyPlayerSet;
    }

    public static void setGetnearbyPlayer(HashSet<Player> getnearbyPlayer) {
        getnearbyPlayerSet = getnearbyPlayer;
    }

    public static HashSet<Player> getEntityspawnPlayerSet() {
        return entityspawnPlayerSet;
    }

    public static void setEntityspawnPlayerSet(HashSet<Player> entityspawnPlayerSet) {
        Kinematics.entityspawnPlayerSet = entityspawnPlayerSet;
    }

    public static Vector getexplosionTolerance() {
        return explosionTolerance;
    }

    public static void setexplosionTolerance(Vector xtol) {
        explosionTolerance = xtol;
    }

    public static boolean isShowExplosionsonGround() {
        return showExplosionsonGround;
    }

    public static void setShowExplosionsonGround(boolean showExplosionsonGround) {
        Kinematics.showExplosionsonGround = showExplosionsonGround;
    }

    public static int getMinExplosions() {
        return minExplosions;
    }

    public static void setMinExplosions(int minExplosions) {
        Kinematics.minExplosions = minExplosions;
    }

    public static int getMinStacking() {
        return minStacking;
    }

    public static void setMinStacking(int minStacking) {
        Kinematics.minStacking = minStacking;
    }

    public static int getMinNearby() {
        return minNearby;
    }

    public static void setMinNearby(int minNearby) {
        Kinematics.minNearby = minNearby;
    }

    public static int getDecimals() {
        return decimals;
    }

    public static void setDecimals(int decimals) {
        Kinematics.decimals = decimals;
    }

    public static String getExplosionstemplate() {
        return explosionstemplate;
    }

    public static String getStackingtemplate() {
        return stackingtemplate;
    }

    public static String getGetnearybtemplate() {
        return getnearybtemplate;
    }

    public static String getEntityspawntempalte() {
        return entityspawntempalte;
    }

    public static boolean isProtection() {
        return protection;
    }

    public static void setProtection(boolean protection) {
        Kinematics.protection = protection;
    }

    public static HashSet<Material> getNotProtected() {
        return notProtected;
    }

    public static void setNotProtected(HashSet<Material> notProtected) {
        Kinematics.notProtected = notProtected;
    }

    private static class TickLoop implements Runnable {
        @Override
        public void run() {
            for (World w : Bukkit.getServer().getWorlds()) {
                for (Entity e : w.getEntities()) {
                    if (e.getTicksLived() != 1 || !e.getType().equals(EntityType.PRIMED_TNT) && !e.getType().equals(EntityType.FALLING_BLOCK))
                        continue;
                    for (Player p : entityspawnPlayerSet) {
                        p.sendMessage(Util.replaceAll(entityspawntempalte, e.getLocation().toVector(), e.getVelocity(), tick, -1, -1, e.getName().toLowerCase()));
                    }
                }
            }

            for (Kevent e : events) {
                Kexplosion exp;
                if (!e.isIgnored()) {
                    for (Player p : e.getPlayers()) {
                        p.sendMessage(e.toTextComponent());
                    }
                }
                if (getnearbyPlayerSet.isEmpty() || !(e instanceof Kexplosion) || (exp = (Kexplosion) e).isSwinging() || exp.amount() < minNearby)
                    continue;
                for (Vector vec : exp.getnearby()) {
                    vec.subtract(exp.getpos());
                    double d = vec.length();
                    if (!(d > 0.0) || !(d < 8.0)) continue;
                    Vector rel = vec.clone().multiply((-0.125 * d + 1.0) / d);
                    for (Player p : getnearbyPlayerSet) {
                        p.sendMessage(Util.replaceAll(getnearybtemplate, vec, rel, tick, -1, -1, "").replaceAll("@d", String.valueOf(Util.round(d))).replaceAll("@vd", "(" + Util.round(1.0 - 0.125 * d) + ")"));
                    }
                }
            }

            order = 0;
            events.clear();
            tick = tick + 1;
        }
    }
}

