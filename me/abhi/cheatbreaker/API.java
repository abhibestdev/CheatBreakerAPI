package me.abhi.cheatbreaker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class API extends JavaPlugin {

    public static boolean kick;
    public static String serverRestartMessage;
    public static String recommendationMessage;
    public static String kickMessage;
    public static String welcomeMessage;
    public static String noPermissionMessage;
    public static String couldNotFindPlayerMessage;
    public static String playerIsOnCheatBreakerMessage;
    public static String playerIsNotOnCheatBreakerMessage;
    public static List<UUID> cheatbreakerUsers;

    @Override
    public void onEnable() {
        load();
        cheatbreakerUsers = new ArrayList<>();
        register();
    }

    @Override
    public void onDisable() {
        for (Player all : getServer().getOnlinePlayers()) {
            all.kickPlayer(serverRestartMessage);
        }
    }

    private void load() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        kick = getConfig().getBoolean("kick");
        serverRestartMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("server-restart"));
        recommendationMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("recommendation-message"));
        kickMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("kick-message"));
        welcomeMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("welcome-message"));
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-permission"));
        couldNotFindPlayerMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("could-not-find-player"));
        playerIsOnCheatBreakerMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("player-is-on-cheatbreaker"));
        playerIsNotOnCheatBreakerMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("player-is-not-on-cheatbreaker"));
    }

    private void register() {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "MC|Brand", new PlayerListener());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        ((CraftServer) getServer()).getCommandMap().register("checkclient", new CheckClientCommand());
    }
}
