package com.enderdevelopment.plugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public KitManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "kits.yml");
        if (!file.exists()) {
            plugin.saveResource("kits.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        setupDefaultKits();
    }

    private void setupDefaultKits() {
        if (config.getConfigurationSection("kits") == null) {
            config.set("kits.starter.name", "Starter");
            config.set("kits.starter.cooldown", 3600);
            config.set("kits.starter.items", null);
            config.set("kits.starter.0.material", "STONE_SWORD");
            config.set("kits.starter.0.amount", 1);
            config.set("kits.starter.1.material", "BREAD");
            config.set("kits.starter.1.amount", 8);
            config.set("kits.starter.2.material", "LEATHER_HELMET");
            config.set("kits.starter.2.amount", 1);
            config.set("kits.starter.3.material", "LEATHER_CHESTPLATE");
            config.set("kits.starter.3.amount", 1);
            config.set("kits.starter.4.material", "LEATHER_LEGGINGS");
            config.set("kits.starter.4.amount", 1);
            config.set("kits.starter.5.material", "LEATHER_BOOTS");
            config.set("kits.starter.5.amount", 1);

            config.set("kits.miner.name", "Miner");
            config.set("kits.miner.cooldown", 7200);
            config.set("kits.miner.0.material", "IRON_PICKAXE");
            config.set("kits.miner.0.amount", 1);
            config.set("kits.miner.1.material", "TORCH");
            config.set("kits.miner.1.amount", 32);
            config.set("kits.miner.2.material", "COBWEB");
            config.set("kits.miner.2.amount", 4);

            config.set("kits.farmer.name", "Farmer");
            config.set("kits.farmer.cooldown", 7200);
            config.set("kits.farmer.0.material", "IRON_HOE");
            config.set("kits.farmer.0.amount", 1);
            config.set("kits.farmer.1.material", "WHEAT_SEEDS");
            config.set("kits.farmer.1.amount", 64);
            config.set("kits.farmer.2.material", "MELON_SEEDS");
            config.set("kits.farmer.2.amount", 64);
            config.set("kits.farmer.3.material", "PUMPKIN_SEEDS");
            config.set("kits.farmer.3.amount", 64);

            save();
        }
    }

    public java.util.Set<String> config_getKitNames() {
        if (config.getConfigurationSection("kits") == null) return java.util.Set.of();
        return config.getConfigurationSection("kits").getKeys(false);
    }

    public boolean kitExists(String name) {
        return config.contains("kits." + name);
    }

    public boolean hasKit(String name) {
        return kitExists(name);
    }

    public String getKitName(String name) {
        return config.getString("kits." + name + ".name", name);
    }

    public long getCooldown(String name) {
        return config.getLong("kits." + name + ".cooldown", 3600);
    }

    public boolean onCooldown(Player player, String name) {
        var playerCooldowns = cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        if (!playerCooldowns.containsKey(name)) return false;
        long cooldownTime = getCooldown(name) * 1000L;
        long elapsed = System.currentTimeMillis() - playerCooldowns.get(name);
        return elapsed < cooldownTime;
    }

    public long getRemainingCooldown(Player player, String name) {
        var playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null || !playerCooldowns.containsKey(name)) return 0;
        long cooldownTime = getCooldown(name) * 1000L;
        long elapsed = System.currentTimeMillis() - playerCooldowns.get(name);
        return Math.max(0, (cooldownTime - elapsed) / 1000);
    }

    public void applyKit(Player player, String name) {
        var playerCooldowns = cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        playerCooldowns.put(name, System.currentTimeMillis());
        String base = "kits." + name;
        for (String key : config.getConfigurationSection(base).getKeys(false)) {
            if (key.equals("name") || key.equals("cooldown")) continue;
            String materialName = config.getString(base + "." + key + ".material");
            if (materialName == null) continue;
            Material material = Material.getMaterial(materialName);
            if (material == null) continue;
            int amount = config.getInt(base + "." + key + ".amount", 1);
            player.getInventory().addItem(new ItemStack(material, amount));
        }
    }

    public void saveAll() {
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save kits.yml: " + e.getMessage());
        }
    }
}
