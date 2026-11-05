package com.enderdevelopment.plugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommands implements CommandExecutor {

    private final Main plugin;

    public WarpCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "warp":
                return handleWarp(player, args);
            case "warps":
                return handleWarps(player);
            case "setwarp":
                return handleSetWarp(player, args);
            case "delwarp":
                return handleDelWarp(player, args);
        }
        return false;
    }

    private boolean handleWarp(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /warp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        if (!plugin.getWarp().warpExists(name)) {
            Msg.error(player, "Warp '" + name + "' does not exist.");
            return true;
        }
        Location loc = plugin.getWarp().getWarp(name);
        if (loc == null) {
            Msg.error(player, "Warp location is invalid.");
            return true;
        }
        player.teleport(loc);
        Msg.purple(player, "Teleported to warp '" + name + "'.");
        return true;
    }

    private boolean handleWarps(Player player) {
        var warps = plugin.getWarp().getWarps();
        if (warps.isEmpty()) {
            Msg.info(player, "There are no warps.");
            return true;
        }
        Msg.gold(player, "Available warps:");
        for (String name : warps) {
            Msg.purple(player, " - " + name);
        }
        return true;
    }

    private boolean handleSetWarp(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /setwarp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        if (name.contains(".") || name.contains("/") || name.contains("\\")) {
            Msg.error(player, "Invalid warp name.");
            return true;
        }
        plugin.getWarp().setWarp(name, player.getLocation());
        Msg.success(player, "Warp '" + name + "' created.");
        return true;
    }

    private boolean handleDelWarp(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /delwarp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        if (!plugin.getWarp().warpExists(name)) {
            Msg.error(player, "Warp '" + name + "' does not exist.");
            return true;
        }
        plugin.getWarp().deleteWarp(name);
        Msg.success(player, "Warp '" + name + "' deleted.");
        return true;
    }
}
