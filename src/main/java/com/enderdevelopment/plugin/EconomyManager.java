package com.enderdevelopment.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EconomyManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;

    public static final double STARTING_BALANCE = 100.0;

    public EconomyManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "economy.yml");
        if (!file.exists()) {
            plugin.saveResource("economy.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public double getBalance(Player player) {
        return getBalance(player.getUniqueId());
    }

    public double getBalance(UUID uuid) {
        String key = "balances." + uuid.toString();
        if (!config.contains(key)) {
            config.set(key, STARTING_BALANCE);
            save();
        }
        return config.getDouble(key);
    }

    public void setBalance(Player player, double amount) {
        setBalance(player.getUniqueId(), amount);
    }

    public void setBalance(UUID uuid, double amount) {
        config.set("balances." + uuid.toString(), Math.max(0, amount));
        save();
    }

    public void addBalance(Player player, double amount) {
        addBalance(player.getUniqueId(), amount);
    }

    public void addBalance(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public void removeBalance(Player player, double amount) {
        removeBalance(player.getUniqueId(), amount);
    }

    public void removeBalance(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) - amount);
    }

    public boolean hasBalance(Player player, double amount) {
        return hasBalance(player.getUniqueId(), amount);
    }

    public boolean hasBalance(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save economy.yml: " + e.getMessage());
        }
    }
}
