package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class LobbyManager {

    private final Main plugin;

    public LobbyManager(Main plugin) {
        this.plugin = plugin;
    }

    public void setHubLocation(Location location) {
        plugin.getConfig().set("lobby.spawn", serializeLocation(location));
        plugin.getConfig().set("lobby.set", true);
        plugin.saveConfig();
    }

    public boolean isHubSet() {
        return plugin.getConfig().getBoolean("lobby.set", false);
    }

    public Location getHubLocation() {
        if (!isHubSet()) return null;
        return deserializeLocation(plugin.getConfig().getString("lobby.spawn"));
    }

    public void teleportToHub(Player player) {
        if (!isHubSet()) {
            Msg.error(player, "The lobby has not been set up yet. An admin must use /accepthub first.");
            return;
        }
        Location loc = getHubLocation();
        player.teleport(loc);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        Msg.purple(player, "Welcome to the EnderMedieval Hub!");
    }

    public void onPlayerJoin(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, createItem(org.bukkit.Material.COMPASS, "Warp Menu", NamedTextColor.LIGHT_PURPLE));
        player.getInventory().setItem(1, createItem(org.bukkit.Material.CHEST, "Shop", NamedTextColor.LIGHT_PURPLE));
        player.getInventory().setItem(2, createItem(org.bukkit.Material.GOLD_INGOT, "Auction House", NamedTextColor.GOLD));
        player.getInventory().setItem(3, createItem(org.bukkit.Material.SKELETON_SKULL, "Bounties", NamedTextColor.RED));
        player.getInventory().setItem(4, createItem(org.bukkit.Material.RED_BED, "Return to Hub", NamedTextColor.LIGHT_PURPLE));
        player.setAllowFlight(false);
        player.setFlying(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0, false, false));
    }

    public void spawnNPCS() {
        if (!isHubSet()) return;
        Location hub = getHubLocation();
        World world = hub.getWorld();
        if (world == null) return;

        spawnVillager(world, hub.clone().add(3, 0, 0), "Shop Keeper");
        spawnVillager(world, hub.clone().add(-3, 0, 0), "Auctioneer");
        spawnVillager(world, hub.clone().add(0, 0, 3), "Bounty Master");
    }

    private void spawnVillager(World world, Location loc, String name) {
        Villager villager = (Villager) world.spawnEntity(loc, EntityType.VILLAGER);
        villager.customName(Component.text(name).color(NamedTextColor.LIGHT_PURPLE));
        villager.setCustomNameVisible(true);
        villager.setAI(false);
        villager.setInvulnerable(true);
        villager.setGravity(false);
    }

    private org.bukkit.inventory.ItemStack createItem(org.bukkit.Material material, String name, NamedTextColor color) {
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name).color(color));
            item.setItemMeta(meta);
        }
        return item;
    }

    private String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    private Location deserializeLocation(String data) {
        String[] parts = data.split(",");
        if (parts.length < 6) return null;
        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;
        return new Location(world,
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5]));
    }
}
