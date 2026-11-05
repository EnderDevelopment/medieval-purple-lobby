package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommands implements CommandExecutor {

    private final Main plugin;

    public KitCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("kits")) {
            Msg.gold(player, "Available kits:");
            for (String name : plugin.getKit().config_getKitNames()) {
                Msg.purple(player, " - " + name + " (" + plugin.getKit().getKitName(name) + ")");
            }
            return true;
        }

        if (cmd.equals("kit")) {
            if (args.length < 1) {
                Msg.error(player, "Usage: /kit <name>");
                return true;
            }
            String name = args[0].toLowerCase();
            if (!plugin.getKit().hasKit(name)) {
                Msg.error(player, "Kit '" + name + "' does not exist.");
                return true;
            }
            if (plugin.getKit().onCooldown(player, name)) {
                long remaining = plugin.getKit().getRemainingCooldown(player, name);
                Msg.error(player, "Kit is on cooldown. Wait " + remaining + " more seconds.");
                return true;
            }
            plugin.getKit().applyKit(player, name);
            Msg.success(player, "You have claimed the " + plugin.getKit().getKitName(name) + " kit!");
            return true;
        }
        return false;
    }
}
