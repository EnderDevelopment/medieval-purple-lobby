package com.enderdevelopment.plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class RTPCommand implements CommandExecutor {

    private final Main plugin;
    private final Random random = new Random();
    private static final int RTP_RADIUS = 10000;

    public RTPCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        World world = player.getWorld();
        Location loc = findSafeLocation(world);
        if (loc == null) {
            Msg.error(player, "Could not find a safe location. Try again.");
            return true;
        }
        player.teleport(loc);
        Msg.purple(player, "Teleported to a random location!");
        return true;
    }

    private Location findSafeLocation(World world) {
        for (int attempt = 0; attempt < 20; attempt++) {
            int x = random.nextInt(RTP_RADIUS * 2) - RTP_RADIUS;
            int z = random.nextInt(RTP_RADIUS * 2) - RTP_RADIUS;
            int y = world.getHighestBlockYAt(x, z);
            Location loc = new Location(world, x + 0.5, y + 1, z + 0.5);
            if (isSafeLocation(loc)) {
                return loc;
            }
        }
        return null;
    }

    private boolean isSafeLocation(Location loc) {
        Block block = loc.getBlock();
        Block below = loc.clone().subtract(0, 1, 0).getBlock();
        Block above = loc.clone().add(0, 1, 0).getBlock();
        return !block.getType().isAir()
                || (below.getType().isSolid() && !below.isLiquid())
                && (above.getType().isAir() || !above.getType().isSolid());
    }
}
