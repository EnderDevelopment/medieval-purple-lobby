package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SocialCommands implements CommandExecutor {

    private final Main plugin;

    public SocialCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("msg")) {
            if (args.length < 2) {
                Msg.error(player, "Usage: /msg <player> <message>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                Msg.error(player, "Player not found.");
                return true;
            }
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) message.append(" ");
                message.append(args[i]);
            }
            player.sendMessage(net.kyori.adventure.text.Component.text("[" + player.getName() + " -> " + target.getName() + "] ")
                    .color(net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE)
                    .append(net.kyori.adventure.text.Component.text(message.toString())
                            .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)));
            target.sendMessage(net.kyori.adventure.text.Component.text("[" + player.getName() + " -> " + target.getName() + "] ")
                    .color(net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE)
                    .append(net.kyori.adventure.text.Component.text(message.toString())
                            .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)));
            plugin.getTeleport().setLastMessaged(player, target.getUniqueId());
            plugin.getTeleport().setLastMessaged(target, player.getUniqueId());
            return true;
        }

        if (cmd.equals("reply")) {
            UUID last = plugin.getTeleport().getLastMessaged(player);
            if (last == null || Bukkit.getPlayer(last) == null) {
                Msg.error(player, "You have no one to reply to.");
                return true;
            }
            if (args.length < 1) {
                Msg.error(player, "Usage: /reply <message>");
                return true;
            }
            Player target = Bukkit.getPlayer(last);
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) message.append(" ");
                message.append(args[i]);
            }
            player.sendMessage(net.kyori.adventure.text.Component.text("[" + player.getName() + " -> " + target.getName() + "] ")
                    .color(net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE)
                    .append(net.kyori.adventure.text.Component.text(message.toString())
                            .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)));
            target.sendMessage(net.kyori.adventure.text.Component.text("[" + player.getName() + " -> " + target.getName() + "] ")
                    .color(net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE)
                    .append(net.kyori.adventure.text.Component.text(message.toString())
                            .color(net.kyori.adventure.text.format.NamedTextColor.WHITE)));
            return true;
        }
        return false;
    }
}
