package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.configuration.Configuration;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.ArrayUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class KinematicsCommand implements CommandExecutor, TabCompleter {
    private final List<KinematicsCommand> subCommands = new ArrayList<>();
    private final Map<Class<? extends KinematicsCommand>, KinematicsCommand> subCommandsByClass = new HashMap<>();
    private final String name;
    private final List<String> aliases;
    private final Kinematics plugin;

    public KinematicsCommand(Kinematics plugin, String name) {
        this(plugin, name, List.of());
    }

    public KinematicsCommand(Kinematics plugin, String name, List<String> aliases) {
        this.plugin = plugin;
        this.aliases = aliases;
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public final List<String> getAliases() {
        return aliases;
    }

    public final Kinematics getPlugin() {
        return plugin;
    }

    public final Configuration getConfig() {
        return plugin.getConfiguration();
    }

    public final boolean isCommand(String name) {
        return this.name.equals(name) || this.aliases.contains(name);
    }

    public boolean showInHelpMessage() {
        return true;
    }

    public boolean isTabCompletable() {
        return true;
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command fullCommand, @NotNull String s, @NotNull String[] args) {
        try {
            this.baseCommand(sender, ArrayUtil.arraytolowercase(args));
        } catch (CommandException e) {
            this.announceException(sender, e);
        }

        return true;
    }

    protected final void announceException(CommandSender sender, CommandException e) {
        Audience audience = this.plugin.getAdventure().sender(sender);
        audience.sendMessage(MiniMessage.miniMessage().deserialize(e.getMessage()));
    }

    public @Nullable String description() {
        return null;
    }

    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    public abstract void execute(KinematicsPlayer kplayer, String[] args) throws CommandException;

    protected void baseCommand(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (args.length > 0) {
            for (KinematicsCommand sub : this.subCommands) {
                if (args[0].equalsIgnoreCase(sub.getName()) || sub.getAliases().contains(args[0])) {
                    sub.baseCommand(sender, ArrayUtil.shiftarray(args));
                    return;
                }
            }
        }

        if (sender instanceof Player player) {
            this.execute(getPlugin().getPlayerManager().wrap(player), args);
        } else {
            this.execute(sender, args);
        }
    }

    @Override
    public final @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String fullCommand, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return null;
        } else {
            return tabComplete(plugin.getPlayerManager().wrap(player), fullCommand, ArrayUtil.arraytolowercase(args));
        }
    }

    public List<String> tabComplete(KinematicsPlayer kplayer, String fullCommand, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        for (KinematicsCommand sub : this.subCommands) {
            if (args.length > 0 && (args[0].equalsIgnoreCase(sub.getName()) || sub.getAliases().contains(args[0].toLowerCase()))) {
                return sub.tabComplete(kplayer, args[0], ArrayUtil.shiftarray(args));
            } else if (!sub.isTabCompletable()) {
                continue;
            }

            String search = args.length > 0 ? args[0] : fullCommand;
            List<String> aliases = args.length > 0 ? this.getAliases() : sub.getAliases();

            if (sub.getName().startsWith(search)) {
                list.add(sub.getName());
                continue;
            }

            for (String ali : aliases) {
                if (!ali.startsWith(search)) continue;
                list.add(ali);
                break;
            }
        }

        return list;
    }

    public final List<KinematicsCommand> getSubCommands() {
        return subCommands;
    }

    public final @Nullable KinematicsCommand getSub(Class<? extends KinematicsCommand> command) {
        return this.subCommandsByClass.get(command);
    }

    public final void addSub(KinematicsCommand cmd) {
        this.subCommands.add(cmd);
        this.subCommandsByClass.put(cmd.getClass(), cmd);
    }

    protected static int parsInt(String arg) throws CommandException {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            throw new CommandException("Unable to pars " + arg + " to Integer");
        }
    }

    protected static double parsDouble(String arg) throws CommandException {
        try {
            return Double.parseDouble(arg);
        } catch (Exception e) {
            throw new CommandException("Unable to pars " + arg + " to floating point number");
        }
    }

    protected static Material parsMaterial(String arg) throws CommandException {
        try {
            return Material.valueOf(arg.toUpperCase());
        } catch (Exception e) {
            throw new CommandException("no material: " + arg);
        }
    }

    protected static List<String> listOfNumbers() {
        return IntStream.range(0, 10)
                .mapToObj(String::valueOf)
                .toList();
    }
}

