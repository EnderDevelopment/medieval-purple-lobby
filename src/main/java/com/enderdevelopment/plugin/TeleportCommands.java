package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommands implements CommandExecutor {

    private final Main plugin;

    public TeleportCommands(Main plugin) {
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
            case "tpa":
                return handleTpa(player, args);
            case "tpahere":
                return handleTpaHere(player, args);
            case "tpaccept":
                return handleTpAccept(player);
            case "tpdeny":
                return handleTpDeny(player);
        }
        return false;
    }

    private boolean handleTpa(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /tpa <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Msg.error(player, "Player not found.");
            return true;
        }
        if (target == player) {
            Msg.error(player, "You cannot teleport to yourself.");
            return true;
        }
        plugin.getTeleport().createRequest(player, target, false);
        Msg.purple(player, "Teleport request sent to " + target.getName() + ".");
        Msg.purple(target, player.getName() + " wants to teleport to you. Use /tpaccept to accept.");
        return true;
    }

    private boolean handleTpaHere(Player player, String[] args) {
        if (args.length < 1) {
            Msg.error(player, "Usage: /tpahere <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Msg.error(player, "Player not found.");
            return true;
        }
        if (target == player) {
            Msg.error(player, "You cannot request this to yourself.");
            return true;
        }
        plugin.getTeleport().createRequest(player, target, true);
        Msg.purple(player, "Teleport request sent to " + target.getName() + ".");
        Msg.purple(target, player.getName() + " wants you to teleport to them. Use /tpaccept to accept.");
        return true;
    }

    private boolean handleTpAccept(Player player) {
        var request = plugin.getTeleport().getRequest(player);
        if (request == null) {
            Msg.error(player, "You have no pending teleport requests.");
            return true;
        }
        Player requester = Bukkit.getPlayer(request.requester());
        if (requester == null) {
            Msg.error(player, "The requester is no longer online.");
            plugin.getTeleport().removeRequest(player);
            return true;
        }
        if (request.here()) {
            player.teleport(requester.getLocation());
        } else {
            requester.teleport(player.getLocation());
        }
        Msg.success(player, "Teleport accepted.");
        Msg.success(requester, "Teleport request accepted!");
        plugin.getTeleport().removeRequest(player);
        return true;
    }

    private boolean handleTpDeny(Player player) {
        var request = plugin.getTeleport().getRequest(player);
        if (request == null) {
            Msg.error(player, "You have no pending teleport requests.");
            return true;
        }
        Player requester = Bukkit.getPlayer(request.requester());
        if (requester != null) {
            Msg.info(requester, "Your teleport request was denied.");
        }
        Msg.success(player, "Teleport request denied.");
        plugin.getTeleport().removeRequest(player);
        return true;
    }
}
