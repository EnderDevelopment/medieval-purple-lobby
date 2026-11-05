package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionCommands implements CommandExecutor {

    private final Main plugin;

    public AuctionCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("auction")) {
            return handleAuction(player, args);
        }
        if (cmd.equals("bid")) {
            if (args.length < 2) {
                Msg.error(player, "Usage: /bid <auctionId> <amount>");
                return true;
            }
            try {
                int id = Integer.parseInt(args[0]);
                double amount = Double.parseDouble(args[1]);
                if (!plugin.getEconomy().hasBalance(player, amount)) {
                    Msg.error(player, "You do not have enough money.");
                    return true;
                }
                if (plugin.getAuction().bid(player, id, amount)) {
                    plugin.getEconomy().removeBalance(player, amount);
                    Msg.success(player, "You bid $" + String.format("%.2f", amount) + " on auction #" + id + ".");
                } else {
                    Msg.error(player, "Bid failed. Auction may not exist or your bid is too low.");
                }
            } catch (NumberFormatException e) {
                Msg.error(player, "Invalid numbers.");
            }
            return true;
        }
        return false;
    }

    private boolean handleAuction(Player player, String[] args) {
        if (args.length == 0) {
            plugin.getAuction().openAuctionHouse(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("sell")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || item.getType().isAir()) {
                Msg.error(player, "Hold an item to sell.");
                return true;
            }
            if (args.length < 2) {
                Msg.error(player, "Usage: /auction sell <price>");
                return true;
            }
            try {
                double price = Double.parseDouble(args[1]);
                if (price <= 0) {
                    Msg.error(player, "Price must be positive.");
                    return true;
                }
                int id = plugin.getAuction().createAuction(player, item.clone(), price);
                player.getInventory().setItemInMainHand(null);
                Msg.success(player, "Your item is now on auction #" + id + " for $" + String.format("%.2f", price) + ".");
            } catch (NumberFormatException e) {
                Msg.error(player, "Invalid price.");
            }
            return true;
        }
        Msg.error(player, "Usage: /auction [sell <price>]");
        return true;
    }
}
