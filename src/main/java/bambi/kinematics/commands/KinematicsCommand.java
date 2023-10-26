package bambi.kinematics.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class KinematicsCommand implements CommandExecutor, TabCompleter {
    private final String name;
    private final HashSet<KinematicsCommand> subcommands = new HashSet<>();
    private final HashSet<String> aliases = new HashSet<>();
    private static final String prefix = ChatColor.AQUA + "Kinematics " + ChatColor.GRAY;

    public KinematicsCommand(String name) {
        this.name = name.toLowerCase();
    }

    public abstract void execute(CommandSender var1, String[] var2) throws CommandException;

    public abstract void execute(Player var1, String[] var2) throws CommandException;

    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length > 0) {
            for (KinematicsCommand sub : this.subcommands) {
                if (!args[0].equalsIgnoreCase(sub.getName()) && !sub.getAliases().contains(args[0].toLowerCase()))
                    continue;
                sub.execute(sender, args[0], KinematicsCommand.shiftarray(args));
                return;
            }
        }

        if (sender instanceof Player) {
            this.execute((Player) sender, args);
        } else {
            this.execute(sender, args);
        }
    }

    public List<String> TabComplete(CommandSender sender, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length > 0) {
            block0:
            for (KinematicsCommand sub : this.subcommands) {
                if (args[0].equalsIgnoreCase(sub.getName()) || sub.getAliases().contains(args[0].toLowerCase())) {
                    return sub.TabComplete(sender, args[0], KinematicsCommand.shiftarray(args));
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
            if (sub.getName().startsWith(label)) {
                list.add(sub.getName());
                continue;
            }
            for (String ali : sub.getAliases()) {
                if (!ali.startsWith(label)) continue;
                list.add(ali);
                continue block2;
            }
        }

        return list;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            this.execute(sender, label, KinematicsCommand.arraytolowercase(args));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.TabComplete(sender, label, KinematicsCommand.arraytolowercase(args));
    }

    public String getName() {
        return this.name;
    }

    public void addSub(KinematicsCommand cmd) {
        this.subcommands.add(cmd);
    }

    public HashSet<KinematicsCommand> getSubcommands() {
        return this.subcommands;
    }

    public void addAlias(String alias) {
        this.aliases.add(alias.toLowerCase());
    }

    public HashSet<String> getAliases() {
        return this.aliases;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static int parsInt(String arg) throws CommandException {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            throw new CommandException("Unable to pars " + arg + " to Integer");
        }
    }

    public static double parsDouble(String arg) throws CommandException {
        try {
            return Double.parseDouble(arg);
        } catch (Exception e) {
            throw new CommandException("Unable to pars " + arg + " to floating point number");
        }
    }

    public static Material parsMaterial(String arg) throws CommandException {
        try {
            return Material.valueOf(arg.toUpperCase());
        } catch (Exception e) {
            throw new CommandException("no material: " + arg);
        }
    }

    public static String[] shiftarray(String[] array) {
        String[] newarray = new String[array.length - 1];
        for (int i = array.length - 1; i > 0; --i) {
            newarray[i - 1] = array[i];
        }
        return newarray;
    }

    public static String[] arraytolowercase(String[] array) {
        String[] newarray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newarray[i] = array[i].toLowerCase();
        }
        return newarray;
    }
}

