package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class WarpManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;

    public WarpManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "warps.yml");
        if (!file.exists()) {
            plugin.saveResource("warps.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setWarp(String name, Location loc) {
        config.set("warps." + name + ".world", loc.getWorld().getName());
        config.set("warps." + name + ".x", loc.getX());
        config.set("warps." + name + ".y", loc.getY());
        config.set("warps." + name + ".z", loc.getZ());
        config.set("warps." + name + ".yaw", loc.getYaw());
        config.set("warps." + name + ".pitch", loc.getPitch());
        save();
    }

    public Location getWarp(String name) {
        String base = "warps." + name;
        if (!config.contains(base)) return null;
        String world = config.getString(base + ".world");
        if (world == null || Bukkit.getWorld(world) == null) return null;
        double x = config.getDouble(base + ".x");
        double y = config.getDouble(base + ".y");
        double z = config.getDouble(base + ".z");
        float yaw = (float) config.getDouble(base + ".yaw");
        float pitch = (float) config.getDouble(base + ".pitch");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public boolean warpExists(String name) {
        return config.contains("warps." + name);
    }

    public Set<String> getWarps() {
        if (config.getConfigurationSection("warps") == null) return Set.of();
        return config.getConfigurationSection("warps").getKeys(false);
    }

    public void deleteWarp(String name) {
        config.set("warps." + name, null);
        save();
    }

    public void saveAll() {
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save warps.yml: " + e.getMessage());
        }
    }
}
