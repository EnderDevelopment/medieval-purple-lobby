package com.enderdevelopment.plugin;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {

    private final Main plugin;

    public AdminCommands(Main plugin) {
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
            case "fly":
                return handleFly(player);
            case "god":
                return handleGod(player);
            case "heal":
                return handleHeal(player);
            case "feed":
                return handleFeed(player);
            case "gamemode":
                return handleGamemode(player, args);
        }
        return false;
    }

    private boolean handleFly(Player player) {
        if (!player.hasPermission("endermedieval.admin.fly")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        boolean flying = !player.getAllowFlight();
        player.setAllowFlight(flying);
        player.setFlying(flying);
        if (flying) {
            Msg.success(player, "Flight enabled.");
        } else {
            Msg.success(player, "Flight disabled.");
        }
        return true;
    }

    private boolean handleGod(Player player) {
        if (!player.hasPermission("endermedieval.admin.god")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        boolean god = !player.isInvulnerable();
        player.setInvulnerable(god);
        if (god) {
            Msg.success(player, "God mode enabled.");
        } else {
            Msg.success(player, "God mode disabled.");
        }
        return true;
    }

    private boolean handleHeal(Player player) {
        if (!player.hasPermission("endermedieval.admin.heal")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        player.setHealth(player.getMaxHealth());
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        Msg.success(player, "You have been healed.");
        return true;
    }

    private boolean handleFeed(Player player) {
        if (!player.hasPermission("endermedieval.admin.feed")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        player.setFoodLevel(20);
        player.setSaturation(20);
        Msg.success(player, "You have been fed.");
        return true;
    }

    private boolean handleGamemode(Player player, String[] args) {
        if (!player.hasPermission("endermedieval.admin.gamemode")) {
            Msg.error(player, "You do not have permission.");
            return true;
        }
        if (args.length < 1) {
            Msg.error(player, "Usage: /gamemode <creative|survival|adventure|spectator>");
            return true;
        }
        GameMode mode = switch (args[0].toLowerCase()) {
            case "creative", "c", "1" -> GameMode.CREATIVE;
            case "survival", "s", "0" -> GameMode.SURVIVAL;
            case "adventure", "a", "2" -> GameMode.ADVENTURE;
            case "spectator", "sp", "3" -> GameMode.SPECTATOR;
            default -> null;
        };
        if (mode == null) {
            Msg.error(player, "Invalid gamemode.");
            return true;
        }
        player.setGameMode(mode);
        Msg.success(player, "Gamemode set to " + mode.name().toLowerCase() + ".");
        return true;
    }
}
