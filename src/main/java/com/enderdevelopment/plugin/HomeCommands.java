package com.enderdevelopment.plugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommands implements CommandExecutor {

    private final Main plugin;

    public HomeCommands(Main plugin) {
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
            case "sethome":
                return handleSetHome(player, args);
            case "home":
                return handleHome(player, args);
            case "homes":
                return handleHomes(player);
            case "delhome":
                return handleDelHome(player, args);
        }
        return false;
    }

    private boolean handleSetHome(Player player, String[] args) {
        String name = args.length > 0 ? args[0].toLowerCase() : "home";
        if (name.contains(".") || name.contains("/") || name.contains("\\")) {
            Msg.error(player, "Invalid home name.");
            return true;
        }
        plugin.getHome().setHome(player, name);
        Msg.success(player, "Home '" + name + "' set!");
        return true;
    }

    private boolean handleHome(Player player, String[] args) {
        String name = args.length > 0 ? args[0].toLowerCase() : "home";
        if (!plugin.getHome().hasHome(player, name)) {
            Msg.error(player, "You do not have a home named '" + name + "'.");
            return true;
        }
        Location loc = plugin.getHome().getHome(player, name);
        if (loc == null) {
            Msg.error(player, "Your home location is invalid.");
            return true;
        }
        player.teleport(loc);
        Msg.purple(player, "Teleported to home '" + name + "'.");
        return true;
    }

    private boolean handleHomes(Player player) {
        var homes = plugin.getHome().getHomes(player);
        if (homes.isEmpty()) {
            Msg.info(player, "You have no homes. Use /sethome to create one.");
            return true;
        }
        Msg.gold(player, "Your homes:");
        for (String name : homes.keySet()) {
            Msg.purple(player, " - " + name);
        }
        return true;
    }

    private boolean handleDelHome(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /delhome <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        if (!plugin.getHome().hasHome(player, name)) {
            Msg.error(player, "You do not have a home named '" + name + "'.");
            return true;
        }
        plugin.getHome().deleteHome(player, name);
        Msg.success(player, "Home '" + name + "' deleted.");
        return true;
    }
}
