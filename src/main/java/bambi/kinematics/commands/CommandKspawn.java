package bambi.kinematics.commands;

import bambi.kinematics.Kinematics;
import bambi.kinematics.enums.Alignment;
import bambi.kinematics.enums.Direction;
import bambi.kinematics.player.KinematicsPlayer;
import bambi.kinematics.utils.ArrayUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CommandKspawn extends KinematicsCommand {
    public CommandKspawn(Kinematics plugin) {
        super(plugin, "kspawn", List.of("spawn"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof BlockCommandSender)) {
            throw new CommandException("player or commandblock command only");
        }
        this.execute(((BlockCommandSender) sender).getBlock().getWorld(), args);
    }

    @Override
    public void execute(KinematicsPlayer kplayer, String[] args) throws CommandException {
        this.execute(kplayer.getPlayer().getWorld(), args);
    }

    private void execute(World w, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new CommandException("not enough arguments");
        }

        Vector vec = new Vector();
        String type = findCorrectTypeFromAliases(args[0].toLowerCase());
        int amount = (args = ArrayUtil.shiftarray(args)).length > 3 ? CommandKspawn.parsInt(args[3]) : 1;
        int fuse = args.length > 4 ? CommandKspawn.parsInt(args[4]) : 80;

        for (Alignment alignment : Alignment.values()) {
            String arg = args[alignment.getInt()];
            if (arg.endsWith(alignment.name().toLowerCase())) {
                vec.add(alignment.getVector());
                args[alignment.getInt()] = arg.substring(0, arg.length() - alignment.name().length());
            }
        }

        for (Direction dir : Direction.values()) {
            dir.addvec(vec, CommandKspawn.parsDouble(args[dir.getInt()]));
        }

        Location loc = vec.toLocation(w);

        for (int i = 0; i < amount; ++i) {
            Material material = Material.matchMaterial(type);

            if (material == Material.TNT) {
                TNTPrimed tnt = (TNTPrimed) w.spawnEntity(loc, EntityType.PRIMED_TNT);
                tnt.setVelocity(new Vector().zero());
                tnt.setFuseTicks(fuse);
            } else if (material != null && material.hasGravity()) {
                FallingBlock fb = (FallingBlock) w.spawnEntity(loc, EntityType.FALLING_BLOCK);
                fb.setTicksLived(1);
                fb.setBlockData(material.createBlockData());
                fb.teleport(loc);
            } else {
                throw new CommandException("unable to get entity: " + type);
            }
        }
    }

    private String findCorrectTypeFromAliases(String type) {
        return switch (type) {
            case "tntprimed", "primedtnt" -> "tnt";
            case "white" -> "sand";
            case "red", "redsand" -> "red_sand";
            default -> type;
        };
    }

    @Override
    public List<String> tabComplete(KinematicsPlayer kplayer, String fullCommand, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 0) {
            list.add("tnt");
            list.add("sand");
            return list;
        }

        Player player = kplayer.getPlayer();
        Block target = player.getTargetBlock(null, 8);
        Location loc = target.getType().isAir() ? player.getEyeLocation() : target.getLocation();
        list.add(loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
        return list;
    }
}

