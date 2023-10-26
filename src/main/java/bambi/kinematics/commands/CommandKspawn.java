package bambi.kinematics.commands;

import bambi.kinematics.enums.Alignment;
import bambi.kinematics.enums.Direction;
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
    public CommandKspawn() {
        super("kspawn");
        this.addAlias("spawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof BlockCommandSender)) {
            throw new CommandException("player or commandblock command only");
        }
        this.execute(((BlockCommandSender) sender).getBlock().getWorld(), args);
    }

    @Override
    public void execute(Player p, String[] args) throws CommandException {
        this.execute(p.getWorld(), args);
    }

    public void execute(World w, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new CommandException("not enough arguments");
        }

        Vector vec = new Vector();
        String type = args[0].toLowerCase();
        int amount = (args = CommandKspawn.shiftarray(args)).length > 3 ? CommandKspawn.parsInt(args[3]) : 1;
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
            switch (type) {
                case "tntprimed", "primedtnt", "tnt" -> {
                    TNTPrimed tnt = (TNTPrimed) w.spawnEntity(loc, EntityType.PRIMED_TNT);
                    tnt.setVelocity(new Vector().zero());
                    tnt.setFuseTicks(fuse);
                }
                case "sand", "white" -> {
                    FallingBlock fallingblock;
                    fallingblock = w.spawnFallingBlock(loc, Material.SAND, (byte) 0);
                    fallingblock.teleport(loc);
                }
                case "red", "redsand" -> {
                    FallingBlock fallingblock;
                    fallingblock = w.spawnFallingBlock(loc, Material.SAND, (byte) 1);
                    fallingblock.teleport(loc);
                }
                case "gravel" -> {
                    FallingBlock fallingblock = w.spawnFallingBlock(loc, Material.GRAVEL, (byte) 0);
                    fallingblock.teleport(loc);
                }
            }
        }

        throw new CommandException("unable to get entity: " + type);
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return null;
        }

        ArrayList<String> list = new ArrayList<>();
        if (args.length == 0) {
            list.add("tnt");
            list.add("sand");
            return list;
        }

        Block target = p.getTargetBlock(null, 8);
        Location loc = target.getType().equals(Material.AIR) ? p.getEyeLocation() : target.getLocation();
        list.add(loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
        return list;
    }
}

