package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AuctionManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;
    private final AtomicInteger auctionIdCounter = new AtomicInteger(1);

    public AuctionManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "auction.yml");
        if (!file.exists()) {
            plugin.saveResource("auction.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    // --- Auction methods ---

    public int createAuction(Player seller, ItemStack item, double startPrice) {
        int id = auctionIdCounter.getAndIncrement();
        config.set("auctions." + id + ".seller", seller.getUniqueId().toString());
        config.set("auctions." + id + ".item", item.serialize());
        config.set("auctions." + id + ".price", startPrice);
        config.set("auctions." + id + ".endTime", System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
        save();
        return id;
    }

    public boolean bid(Player bidder, int auctionId, double amount) {
        String key = "auctions." + auctionId;
        if (!config.contains(key)) {
            return false;
        }
        double currentPrice = config.getDouble(key + ".price");
        if (amount <= currentPrice) {
            return false;
        }
        config.set(key + ".price", amount);
        config.set(key + ".bidder", bidder.getUniqueId().toString());
        save();
        return true;
    }

    public void openAuctionHouse(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Auction House").color(NamedTextColor.LIGHT_PURPLE));
        List<String> auctionIds = new ArrayList<>();
        if (config.getConfigurationSection("auctions") != null) {
            auctionIds.addAll(config.getConfigurationSection("auctions").getKeys(false));
        }
        int slot = 0;
        for (String id : auctionIds) {
            if (slot >= 45) break;
            String key = "auctions." + id;
            if (System.currentTimeMillis() > config.getLong(key + ".endTime")) continue;
            ItemStack item = ItemStack.deserialize(config.getConfigurationSection(key + ".item").getValues(true));
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("Auction ID: " + id).color(NamedTextColor.DARK_PURPLE));
                lore.add(Component.text("Seller: " + getSellerName(config.getString(key + ".seller"))).color(NamedTextColor.GRAY));
                lore.add(Component.text("Current bid: $" + String.format("%.2f", config.getDouble(key + ".price"))).color(NamedTextColor.GOLD));
                if (config.contains(key + ".bidder")) {
                    lore.add(Component.text("Highest bidder: " + getSellerName(config.getString(key + ".bidder"))).color(NamedTextColor.LIGHT_PURPLE));
                }
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(slot, item);
            slot++;
        }
        player.openInventory(inv);
    }

    private String getSellerName(String uuidString) {
        if (uuidString == null) return "Unknown";
        try {
            UUID uuid = UUID.fromString(uuidString);
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) return p.getName();
            return Bukkit.getOfflinePlayer(uuid).getName() == null ? "Unknown" : Bukkit.getOfflinePlayer(uuid).getName();
        } catch (IllegalArgumentException e) {
            return uuidString;
        }
    }

    // --- Bounty methods ---

    public void setBounty(Player placer, Player target, double amount) {
        config.set("bounties." + target.getUniqueId().toString() + ".amount",
                getBounty(target) + amount);
        config.set("bounties." + target.getUniqueId().toString() + ".placedBy." + placer.getUniqueId().toString(),
                amount);
        save();
    }

    public double getBounty(Player target) {
        return config.getDouble("bounties." + target.getUniqueId().toString() + ".amount", 0.0);
    }

    public boolean hasBounty(Player target) {
        return getBounty(target) > 0;
    }

    public void claimBounty(Player claimer, Player target) {
        double bounty = getBounty(target);
        if (bounty <= 0) return;
        plugin.getEconomy().addBalance(claimer, bounty);
        config.set("bounties." + target.getUniqueId().toString(), null);
        save();
    }

    public void showBounties(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Bounties").color(NamedTextColor.LIGHT_PURPLE));
        if (config.getConfigurationSection("bounties") != null) {
            int slot = 0;
            for (String uuidString : config.getConfigurationSection("bounties").getKeys(false)) {
                if (slot >= 18) break;
                double amount = config.getDouble("bounties." + uuidString + ".amount", 0);
                if (amount <= 0) continue;
                String name = getSellerName(uuidString);
                ItemStack item = new ItemStack(Material.SKELETON_SKULL);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.displayName(Component.text(name).color(NamedTextColor.RED));
                    meta.lore(List.of(
                            Component.text("Bounty: $" + String.format("%.2f", amount)).color(NamedTextColor.GOLD),
                            Component.text("Kill to claim!").color(NamedTextColor.GRAY)
                    ));
                    item.setItemMeta(meta);
                }
                inv.setItem(slot, item);
                slot++;
            }
        }
        player.openInventory(inv);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save auction.yml: " + e.getMessage());
        }
    }
}
