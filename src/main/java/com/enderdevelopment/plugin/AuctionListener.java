package com.enderdevelopment.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AuctionListener implements Listener {

    private final Main plugin;

    public AuctionListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;
        if (!plugin.getAuction().hasBounty(victim)) return;

        double bounty = plugin.getAuction().getBounty(victim);
        plugin.getAuction().claimBounty(killer, victim);
        Msg.success(killer, "You claimed the $" + String.format("%.2f", bounty) + " bounty on " + victim.getName() + "!");
        Msg.purple(victim, killer.getName() + " claimed your bounty of $" + String.format("%.2f", bounty) + "!");
    }
}
