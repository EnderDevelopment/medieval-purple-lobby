package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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

public class ShopManager {

    private final Main plugin;
    private final File file;
    private FileConfiguration config;

    public enum Category {
        COMBAT("Combat", Material.DIAMOND_SWORD),
        FOOD("Food", Material.COOKED_BEEF),
        BLOCKS("Blocks", Material.STONE),
        TOOLS("Tools", Material.DIAMOND_PICKAXE),
        POTIONS("Potions", Material.POTION),
        ARMOR("Armor", Material.DIAMOND_CHESTPLATE);

        public final String name;
        public final Material icon;

        Category(String name, Material icon) {
            this.name = name;
            this.icon = icon;
        }
    }

    public ShopManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "shop.yml");
        if (!file.exists()) {
            plugin.saveResource("shop.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        setupDefaultShop();
    }

    private void setupDefaultShop() {
        if (config.getConfigurationSection("shop.combat") == null) {
            config.set("shop.combat.items.diamond_sword.material", "DIAMOND_SWORD");
            config.set("shop.combat.items.diamond_sword.price", 250.0);
            config.set("shop.combat.items.diamond_sword.amount", 1);
            config.set("shop.combat.items.diamond_sword.slot", 0);
            config.set("shop.combat.items.iron_sword.material", "IRON_SWORD");
            config.set("shop.combat.items.iron_sword.price", 80.0);
            config.set("shop.combat.items.iron_sword.amount", 1);
            config.set("shop.combat.items.iron_sword.slot", 1);
            config.set("shop.combat.items.bow.material", "BOW");
            config.set("shop.combat.items.bow.price", 120.0);
            config.set("shop.combat.items.bow.amount", 1);
            config.set("shop.combat.items.bow.slot", 2);
            config.set("shop.combat.items.arrow.material", "ARROW");
            config.set("shop.combat.items.arrow.price", 2.0);
            config.set("shop.combat.items.arrow.amount", 16);
            config.set("shop.combat.items.arrow.slot", 3);

            config.set("shop.food.items.cooked_beef.material", "COOKED_BEEF");
            config.set("shop.food.items.cooked_beef.price", 10.0);
            config.set("shop.food.items.cooked_beef.amount", 1);
            config.set("shop.food.items.cooked_beef.slot", 0);
            config.set("shop.food.items.bread.material", "BREAD");
            config.set("shop.food.items.bread.price", 2.0);
            config.set("shop.food.items.bread.amount", 1);
            config.set("shop.food.items.bread.slot", 1);
            config.set("shop.food.items.golden_apple.material", "GOLDEN_APPLE");
            config.set("shop.food.items.golden_apple.price", 150.0);
            config.set("shop.food.items.golden_apple.amount", 1);
            config.set("shop.food.items.golden_apple.slot", 2);
            config.set("shop.food.items.cake.material", "CAKE");
            config.set("shop.food.items.cake.price", 30.0);
            config.set("shop.food.items.cake.amount", 1);
            config.set("shop.food.items.cake.slot", 3);

            config.set("shop.blocks.items.stone.material", "STONE");
            config.set("shop.blocks.items.stone.price", 1.0);
            config.set("shop.blocks.items.stone.amount", 64);
            config.set("shop.blocks.items.stone.slot", 0);
            config.set("shop.blocks.items.oak_log.material", "OAK_LOG");
            config.set("shop.blocks.items.oak_log.price", 2.0);
            config.set("shop.blocks.items.oak_log.amount", 64);
            config.set("shop.blocks.items.oak_log.slot", 1);
            config.set("shop.blocks.items.cobblestone.material", "COBBLESTONE");
            config.set("shop.blocks.items.cobblestone.price", 0.5);
            config.set("shop.blocks.items.cobblestone.amount", 64);
            config.set("shop.blocks.items.cobblestone.slot", 2);
            config.set("shop.blocks.items.obsidian.material", "OBSIDIAN");
            config.set("shop.blocks.items.obsidian.price", 50.0);
            config.set("shop.blocks.items.obsidian.amount", 8);
            config.set("shop.blocks.items.obsidian.slot", 3);

            config.set("shop.tools.items.diamond_pickaxe.material", "DIAMOND_PICKAXE");
            config.set("shop.tools.items.diamond_pickaxe.price", 300.0);
            config.set("shop.tools.items.diamond_pickaxe.amount", 1);
            config.set("shop.tools.items.diamond_pickaxe.slot", 0);
            config.set("shop.tools.items.diamond_axe.material", "DIAMOND_AXE");
            config.set("shop.tools.items.diamond_axe.price", 300.0);
            config.set("shop.tools.items.diamond_axe.amount", 1);
            config.set("shop.tools.items.diamond_axe.slot", 1);
            config.set("shop.tools.items.diamond_shovel.material", "DIAMOND_SHOVEL");
            config.set("shop.tools.items.diamond_shovel.price", 200.0);
            config.set("shop.tools.items.diamond_shovel.amount", 1);
            config.set("shop.tools.items.diamond_shovel.slot", 2);
            config.set("shop.tools.items.fishing_rod.material", "FISHING_ROD");
            config.set("shop.tools.items.fishing_rod.price", 50.0);
            config.set("shop.tools.items.fishing_rod.amount", 1);
            config.set("shop.tools.items.fishing_rod.slot", 3);

            config.set("shop.armor.items.diamond_helmet.material", "DIAMOND_HELMET");
            config.set("shop.armor.items.diamond_helmet.price", 250.0);
            config.set("shop.armor.items.diamond_helmet.amount", 1);
            config.set("shop.armor.items.diamond_helmet.slot", 0);
            config.set("shop.armor.items.diamond_chestplate.material", "DIAMOND_CHESTPLATE");
            config.set("shop.armor.items.diamond_chestplate.price", 400.0);
            config.set("shop.armor.items.diamond_chestplate.amount", 1);
            config.set("shop.armor.items.diamond_chestplate.slot", 1);
            config.set("shop.armor.items.diamond_leggings.material", "DIAMOND_LEGGINGS");
            config.set("shop.armor.items.diamond_leggings.price", 350.0);
            config.set("shop.armor.items.diamond_leggings.amount", 1);
            config.set("shop.armor.items.diamond_leggings.slot", 2);
            config.set("shop.armor.items.diamond_boots.material", "DIAMOND_BOOTS");
            config.set("shop.armor.items.diamond_boots.price", 200.0);
            config.set("shop.armor.items.diamond_boots.amount", 1);
            config.set("shop.armor.items.diamond_boots.slot", 3);

            config.set("shop.potions.items.health_potion.material", "POTION");
            config.set("shop.potions.items.health_potion.price", 40.0);
            config.set("shop.potions.items.health_potion.amount", 1);
            config.set("shop.potions.items.health_potion.slot", 0);
            config.set("shop.potions.items.swiftness_potion.material", "POTION");
            config.set("shop.potions.items.swiftness_potion.price", 35.0);
            config.set("shop.potions.items.swiftness_potion.amount", 1);
            config.set("shop.potions.items.swiftness_potion.slot", 1);
            config.set("shop.potions.items.ender_pearl.material", "ENDER_PEARL");
            config.set("shop.potions.items.ender_pearl.price", 60.0);
            config.set("shop.potions.items.ender_pearl.amount", 1);
            config.set("shop.potions.items.ender_pearl.slot", 2);

            save();
        }
    }

    public void openShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Medieval Shop").color(NamedTextColor.LIGHT_PURPLE));
        for (int i = 0; i < Category.values().length; i++) {
            Category cat = Category.values()[i];
            ItemStack item = new ItemStack(cat.icon);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(cat.name).color(NamedTextColor.LIGHT_PURPLE));
                meta.lore(List.of(Component.text("Click to browse " + cat.name.toLowerCase() + " items").color(NamedTextColor.GRAY)));
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    public void openCategory(Player player, Category category) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Shop - " + category.name).color(NamedTextColor.LIGHT_PURPLE));
        ConfigurationSection section = config.getConfigurationSection("shop." + category.name().toLowerCase() + ".items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                Material material = Material.getMaterial(itemSection.getString("material", "STONE"));
                if (material == null) continue;
                int amount = itemSection.getInt("amount", 1);
                double price = itemSection.getDouble("price", 1.0);
                int slot = itemSection.getInt("slot", 0);
                ItemStack item = new ItemStack(material, amount);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.displayName(Component.text(material.toString().replace('_', ' ').toLowerCase())
                            .color(NamedTextColor.WHITE));
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("Price: $" + String.format("%.2f", price)).color(NamedTextColor.GOLD));
                    lore.add(Component.text("Click to buy").color(NamedTextColor.GREEN));
                    meta.lore(lore);
                    item.setItemMeta(meta);
                }
                inv.setItem(slot, item);
            }
        }
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("Back to categories").color(NamedTextColor.RED));
            back.setItemMeta(backMeta);
        }
        inv.setItem(26, back);
        player.openInventory(inv);
    }

    public double getItemPrice(Category category, ItemStack item) {
        Material mat = item.getType();
        ConfigurationSection section = config.getConfigurationSection("shop." + category.name().toLowerCase() + ".items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                if (itemSection.getString("material", "").equalsIgnoreCase(mat.name())) {
                    return itemSection.getDouble("price", 1.0);
                }
            }
        }
        return -1;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save shop.yml: " + e.getMessage());
        }
    }
}
