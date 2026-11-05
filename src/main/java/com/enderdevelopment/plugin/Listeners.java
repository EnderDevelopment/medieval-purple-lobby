package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    private final Main plugin;

    public Listeners(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getLobby().isHubSet()) {
            plugin.getLobby().onPlayerJoin(player);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getLobby().teleportToHub(player);
            }, 20L);
        }
        event.joinMessage(Component.text(player.getName() + " joined the medieval realm!").color(NamedTextColor.LIGHT_PURPLE));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(event.getPlayer().getName() + " left the medieval realm.").color(NamedTextColor.LIGHT_PURPLE));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isInHub(event.getPlayer()) && !event.getPlayer().hasPermission("endermedieval.admin.build")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isInHub(event.getPlayer()) && !event.getPlayer().hasPermission("endermedieval.admin.build")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && isInHub(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player && isInHub(player)) {
            event.setCancelled(true);
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().title().toString();

        // Shop categories
        if (title.contains("Medieval Shop")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < ShopManager.Category.values().length) {
                ShopManager.Category cat = ShopManager.Category.values()[slot];
                plugin.getShop().openCategory(player, cat);
            }
            return;
        }

        // Shop category items
        if (title.contains("Shop - ")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;
            if (event.getRawSlot() == 26) {
                plugin.getShop().openShop(player);
                return;
            }
            ShopManager.Category cat = getCategoryFromTitle(title);
            if (cat != null) {
                buyItem(player, cat, clicked);
            }
        }
    }

    private ShopManager.Category getCategoryFromTitle(String title) {
        for (ShopManager.Category cat : ShopManager.Category.values()) {
            if (title.contains(cat.name)) return cat;
        }
        return null;
    }

    private void buyItem(Player player, ShopManager.Category category, ItemStack item) {
        double price = plugin.getShop().getItemPrice(category, item);
        if (price < 0) return;
        if (!plugin.getEconomy().hasBalance(player, price)) {
            Msg.error(player, "You do not have enough money. You need $" + String.format("%.2f", price) + ".");
            return;
        }
        plugin.getEconomy().removeBalance(player, price);
        player.getInventory().addItem(item.clone());
        Msg.success(player, "You bought " + item.getType().toString().toLowerCase().replace('_', ' ') + " for $" + String.format("%.2f", price) + ".");
    }

    private boolean isInHub(Player player) {
        if (!plugin.getLobby().isHubSet()) return false;
        var hub = plugin.getLobby().getHubLocation();
        if (hub == null || hub.getWorld() == null) return false;
        return player.getWorld().equals(hub.getWorld())
                && player.getLocation().distance(hub) < 30;
    }
}
