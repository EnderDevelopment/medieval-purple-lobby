package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommands implements CommandExecutor {

    private final Main plugin;

    public EconomyCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "balance":
                return handleBalance(sender, args);
            case "pay":
                return handlePay(sender, args);
            case "eco":
                return handleEco(sender, args);
        }
        return false;
    }

    private boolean handleBalance(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        if (args.length == 0) {
            double bal = plugin.getEconomy().getBalance(player);
            Msg.gold(player, "Your balance: $" + String.format("%.2f", bal));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Msg.error(player, "Player not found.");
            return true;
        }
        double bal = plugin.getEconomy().getBalance(target);
        Msg.gold(player, target.getName() + "'s balance: $" + String.format("%.2f", bal));
        return true;
    }

    private boolean handlePay(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        if (args.length < 2) {
            Msg.error(player, "Usage: /pay <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Msg.error(player, "Player not found.");
            return true;
        }
        if (target == player) {
            Msg.error(player, "You cannot pay yourself.");
            return true;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
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
        plugin.getEconomy().addBalance(target, amount);
        Msg.success(player, "You paid $" + String.format("%.2f", amount) + " to " + target.getName() + ".");
        Msg.success(target, player.getName() + " paid you $" + String.format("%.2f", amount) + ".");
        return true;
    }

    private boolean handleEco(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player) && sender instanceof CommandSender) {
            if (args.length < 3) {
                sender.sendMessage("Usage: /eco <give|take|set|reset> <player> <amount>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("Player not found.");
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount.");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "give" -> {
                    plugin.getEconomy().addBalance(target, amount);
                    sender.sendMessage("Gave $" + amount + " to " + target.getName());
                }
                case "take" -> {
                    plugin.getEconomy().removeBalance(target, amount);
                    sender.sendMessage("Took $" + amount + " from " + target.getName());
                }
                case "set" -> {
                    plugin.getEconomy().setBalance(target, amount);
                    sender.sendMessage("Set " + target.getName() + "'s balance to $" + amount);
                }
                case "reset" -> {
                    plugin.getEconomy().setBalance(target, EconomyManager.STARTING_BALANCE);
                    sender.sendMessage("Reset " + target.getName() + "'s balance.");
                }
                default -> sender.sendMessage("Usage: /eco <give|take|set|reset> <player> <amount>");
            }
            return true;
        }

        if (!(sender instanceof Player player)) return true;
        if (!player.hasPermission("endermedieval.admin.eco")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        if (args.length < 3) {
            Msg.error(player, "Usage: /eco <give|take|set|reset> <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            Msg.error(player, "Player not found.");
            return true;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            Msg.error(player, "Invalid amount.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give" -> {
                plugin.getEconomy().addBalance(target, amount);
                Msg.success(player, "Gave $" + String.format("%.2f", amount) + " to " + target.getName() + ".");
            }
            case "take" -> {
                plugin.getEconomy().removeBalance(target, amount);
                Msg.success(player, "Took $" + String.format("%.2f", amount) + " from " + target.getName() + ".");
            }
            case "set" -> {
                plugin.getEconomy().setBalance(target, amount);
                Msg.success(player, "Set " + target.getName() + "'s balance to $" + String.format("%.2f", amount) + ".");
            }
            case "reset" -> {
                plugin.getEconomy().setBalance(target, EconomyManager.STARTING_BALANCE);
                Msg.success(player, "Reset " + target.getName() + "'s balance.");
            }
            default -> Msg.error(player, "Usage: /eco <give|take|set|reset> <player> <amount>");
        }
        return true;
    }
}
