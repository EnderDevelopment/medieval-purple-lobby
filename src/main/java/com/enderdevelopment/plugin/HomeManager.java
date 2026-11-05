package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;

    public HomeManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "homes.yml");
        if (!file.exists()) {
            plugin.saveResource("homes.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setHome(Player player, String name) {
        Location loc = player.getLocation();
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".world", loc.getWorld().getName());
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".x", loc.getX());
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".y", loc.getY());
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".z", loc.getZ());
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".yaw", loc.getYaw());
        config.set("homes." + player.getUniqueId().toString() + "." + name + ".pitch", loc.getPitch());
        save();
    }

    public Location getHome(Player player, String name) {
        String base = "homes." + player.getUniqueId().toString() + "." + name;
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

    public boolean hasHome(Player player, String name) {
        return config.contains("homes." + player.getUniqueId().toString() + "." + name);
    }

    public Map<String, Location> getHomes(Player player) {
        Map<String, Location> homes = new HashMap<>();
        String base = "homes." + player.getUniqueId().toString();
        if (config.getConfigurationSection(base) != null) {
            for (String name : config.getConfigurationSection(base).getKeys(false)) {
                Location loc = getHome(player, name);
                if (loc != null) homes.put(name, loc);
            }
        }
        return homes;
    }

    public void deleteHome(Player player, String name) {
        config.set("homes." + player.getUniqueId().toString() + "." + name, null);
        save();
    }

    public void saveAll() {
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save homes.yml: " + e.getMessage());
        }
    }
}
