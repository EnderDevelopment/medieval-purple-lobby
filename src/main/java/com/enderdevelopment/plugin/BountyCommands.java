package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommands implements CommandExecutor {

    private final Main plugin;

    public BountyCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        if (args.length == 0) {
            plugin.getAuction().showBounties(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                Msg.error(player, "Usage: /bounty set <player> <amount>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Msg.error(player, "Player not found.");
                return true;
            }
            if (target == player) {
                Msg.error(player, "You cannot place a bounty on yourself.");
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                Msg.error(player, "Invalid amount.");
                return true;
            }
            if (amount <= 0) {
                Msg.error(player, "Amount must be positive.");
                return true;
            }
            if (!plugin.getEconomy().hasBalance(player, amount)) {
                Msg.error(player, "You do not have enough money.");
                return true;
            }
            plugin.getEconomy().removeBalance(player, amount);
            plugin.getAuction().setBounty(player, target, amount);
            Msg.success(player, "You placed a $" + String.format("%.2f", amount) + " bounty on " + target.getName() + "!");
            Msg.purple(target, player.getName() + " placed a $" + String.format("%.2f", amount) + " bounty on you!");
            return true;
        }
        if (args[0].equalsIgnoreCase("claim")) {
            if (args.length < 2) {
                Msg.error(player, "Usage: /bounty claim <player>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Msg.error(player, "Player not found.");
                return true;
            }
            if (!plugin.getAuction().hasBounty(target)) {
                Msg.error(player, target.getName() + " has no bounty.");
                return true;
            }
            double bounty = plugin.getAuction().getBounty(target);
            plugin.getAuction().claimBounty(player, target);
            Msg.success(player, "You claimed the $" + String.format("%.2f", bounty) + " bounty on " + target.getName() + "!");
            return true;
        }
        Msg.error(player, "Usage: /bounty [set <player> <amount> | claim <player>]");
        return true;
    }
}
