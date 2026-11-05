package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommands implements CommandExecutor {

    private final Main plugin;

    public LobbyCommands(Main plugin) {
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
            case "spawn":
            case "lobby":
                plugin.getLobby().teleportToHub(player);
                return true;
            case "accepthub":
                if (!player.hasPermission("endermedieval.admin.hub")) {
                    Msg.error(player, "You do not have permission.");
                    return true;
                }
                plugin.getLobby().setHubLocation(player.getLocation());
                plugin.getLobby().spawnNPCS();
                Msg.success(player, "Hub location set to your current position!");
                return true;
        }
        return false;
    }
}
