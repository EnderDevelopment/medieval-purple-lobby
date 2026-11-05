package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class Msg {

    public static final String PREFIX = "&8[&dEnder&5Medieval&8] ";

    public static void send(Player player, String message) {
        player.sendMessage(Component.text(PREFIX.replace("&8", "").replace("&d", "").replace("&5", "") + message));
    }

    public static void error(Player player, String message) {
        player.sendMessage(Component.text("[EnderMedieval] ").color(NamedTextColor.DARK_PURPLE)
                .append(Component.text(message).color(NamedTextColor.RED)));
    }

    public static void success(Player player, String message) {
        player.sendMessage(Component.text("[EnderMedieval] ").color(NamedTextColor.DARK_PURPLE)
                .append(Component.text(message).color(NamedTextColor.GREEN)));
    }

    public static void info(Player player, String message) {
        player.sendMessage(Component.text("[EnderMedieval] ").color(NamedTextColor.DARK_PURPLE)
                .append(Component.text(message).color(NamedTextColor.GRAY)));
    }

    public static void purple(Player player, String message) {
        player.sendMessage(Component.text("[EnderMedieval] ").color(NamedTextColor.DARK_PURPLE)
                .append(Component.text(message).color(NamedTextColor.LIGHT_PURPLE)));
    }

    public static void gold(Player player, String message) {
        player.sendMessage(Component.text("[EnderMedieval] ").color(NamedTextColor.DARK_PURPLE)
                .append(Component.text(message).color(NamedTextColor.GOLD)));
    }

    public static String color(String message) {
        return message.replace("&0", "\u00a70").replace("&1", "\u00a71").replace("&2", "\u00a72")
                .replace("&3", "\u00a73").replace("&4", "\u00a74").replace("&5", "\u00a75")
                .replace("&6", "\u00a76").replace("&7", "\u00a77").replace("&8", "\u00a78")
                .replace("&9", "\u00a79").replace("&a", "\u00a7a").replace("&b", "\u00a7b")
                .replace("&c", "\u00a7c").replace("&d", "\u00a7d").replace("&e", "\u00a7e")
                .replace("&f", "\u00a7f").replace("&l", "\u00a7l").replace("&o", "\u00a7o")
                .replace("&n", "\u00a7n").replace("&m", "\u00a7m").replace("&k", "\u00a7k")
                .replace("&r", "\u00a7r");
    }
}
