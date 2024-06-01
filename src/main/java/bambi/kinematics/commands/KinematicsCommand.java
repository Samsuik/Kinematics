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
import java.util.List;

public abstract class KinematicsCommand implements CommandExecutor, TabCompleter {
    private final List<KinematicsCommand> subcommands = new ArrayList<>();
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

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command fullCommand, @NotNull String s, @NotNull String[] args) {
        try {
            this.baseCommand(sender, s, ArrayUtil.arraytolowercase(args));
        } catch (CommandException e) {
            Audience audience = this.plugin.getAdventure().sender(sender);
            audience.sendMessage(MiniMessage.miniMessage().deserialize(e.getMessage()));
        }

        return true;
    }

    public @Nullable String description() {
        return null;
    }

    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    public abstract void execute(KinematicsPlayer kplayer, String[] args) throws CommandException;

    protected void baseCommand(@NotNull CommandSender sender, @NotNull String fullCommand, @NotNull String[] args) throws CommandException {
        if (args.length > 0) {
            for (KinematicsCommand sub : this.subcommands) {
                if (args[0].equalsIgnoreCase(sub.getName()) || sub.getAliases().contains(args[0].toLowerCase())) {
                    sub.baseCommand(sender, args[0], ArrayUtil.shiftarray(args));
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

        if (args.length > 0) {
            block0:
            for (KinematicsCommand sub : this.subcommands) {
                if (args[0].equalsIgnoreCase(sub.getName()) || sub.getAliases().contains(args[0].toLowerCase())) {
                    return sub.tabComplete(kplayer, args[0], ArrayUtil.shiftarray(args));
                }
                if (sub.getName().startsWith(args[0])) {
                    list.add(sub.getName());
                    continue;
                }
                for (String ali : this.getAliases()) {
                    if (!ali.startsWith(args[0])) continue;
                    list.add(ali);
                    continue block0;
                }
            }
            return list;
        }

        block2:
        for (KinematicsCommand sub : this.subcommands) {
            if (sub.getName().startsWith(fullCommand)) {
                list.add(sub.getName());
                continue;
            }
            for (String ali : sub.getAliases()) {
                if (!ali.startsWith(fullCommand)) continue;
                list.add(ali);
                continue block2;
            }
        }

        return list;
    }

    public final List<KinematicsCommand> getSubcommands() {
        return subcommands;
    }

    public final void addSub(KinematicsCommand cmd) {
        this.subcommands.add(cmd);
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
}

