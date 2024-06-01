package bambi.kinematics.commands.other;

import bambi.kinematics.Kinematics;
import bambi.kinematics.commands.CommandException;
import bambi.kinematics.commands.CommandKinematics;
import bambi.kinematics.commands.KinematicsCommand;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.ArrayUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandChain extends KinematicsCommand {
    private final CommandKinematics rootCommand;

    public CommandChain(Kinematics plugin, CommandKinematics root) {
        super(plugin, "->");
        this.rootCommand = root;
    }

    @Override
    public boolean showInHelpMessage() {
        return false;
    }

    @Override
    public boolean isTabCompletable() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {}

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        List<String> chainedCommands = this.chainedCommands(args);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!chainedCommands.isEmpty()) {
                    String command = chainedCommands.remove(0);

                    String trimmedCommand = command.trim();
                    String[] commandParts = trimmedCommand.split(" ");

                    if (commandParts.length == 0) {
                        continue; // missing command
                    }

                    try {
                        if (rootCommand.getSub(CommandWait.class).isCommand(commandParts[0]) && commandParts.length > 1) {
                            Bukkit.getScheduler().runTaskLater(getPlugin(), this, parsInt(commandParts[1]));
                            return;
                        }

                        rootCommand.onCommand(kplayer.getPlayer(), null, "", commandParts);
                    } catch (CommandException e) {
                        announceException(kplayer.getPlayer(), e);
                    }
                }
            }
        };

        Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, 0);
    }

    @Override
    public List<String> tabComplete(KinematicsPlayer kplayer, String fullCommand, String[] args) {
        List<String> chainedCommands = this.chainedCommands(args);

        if (chainedCommands.isEmpty()) {
            return List.of();
        }

        String command = chainedCommands.get(chainedCommands.size() - 1).stripLeading();
        String[] commandArgs = StringUtils.splitPreserveAllTokens(command, " ");
        String commandName = commandArgs.length > 0 ? commandArgs[0] : command;

        List<String> completion = rootCommand.tabComplete(kplayer, commandName, commandArgs);
        return completion == null || completion.isEmpty() ? List.of(getName()) : completion;
    }

    private List<String> chainedCommands(String[] args) {
        String joinedArgs = String.join(" ", args);
        return new ArrayList<>(Arrays.asList(joinedArgs.split(getName())));
    }
}
