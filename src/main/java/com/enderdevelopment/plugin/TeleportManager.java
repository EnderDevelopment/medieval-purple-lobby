package com.enderdevelopment.plugin;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {

    private final Map<UUID, TeleportRequest> requests = new HashMap<>();
    private final Map<UUID, UUID> lastMessaged = new HashMap<>();

    public record TeleportRequest(UUID requester, UUID target, boolean here) {}

    public TeleportManager(Main plugin) {
        plugin.getLogger().info("Teleport system loaded.");
    }

    public void createRequest(Player requester, Player target, boolean here) {
        requests.put(target.getUniqueId(), new TeleportRequest(requester.getUniqueId(), target.getUniqueId(), here));
    }

    public TeleportRequest getRequest(Player player) {
        return requests.get(player.getUniqueId());
    }

    public void removeRequest(Player player) {
        requests.remove(player.getUniqueId());
    }

    public void setLastMessaged(Player player, UUID target) {
        lastMessaged.put(player.getUniqueId(), target);
    }

    public UUID getLastMessaged(Player player) {
        return lastMessaged.get(player.getUniqueId());
    }
}
