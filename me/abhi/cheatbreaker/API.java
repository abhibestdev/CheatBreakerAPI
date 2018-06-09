package me.abhi.cheatbreaker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class API extends JavaPlugin {

    private API instance;
    public static boolean kick;
    public static int kickDelay;
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
        instance = this;
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
        kickDelay = getConfig().getInt("kick-delay");
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
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("checkclient").setExecutor(new CheckClientCommand());
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.CUSTOM_PAYLOAD) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                StructureModifier<String> clientTypes = packet.getStrings();
                boolean cheatbreaker = false;
                for (String string : clientTypes.getValues()) {
                    if (string.equals("CB|INIT") || string.equals("CB-Binary")) {
                        cheatbreaker = true;
                    }
                    if (!cheatbreakerUsers.contains(player.getUniqueId())) {
                        if (cheatbreaker) {
                            player.sendMessage(welcomeMessage);
                            cheatbreakerUsers.add(player.getUniqueId());
                        } else {
                            if (kick) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.kickPlayer(kickMessage);
                                    }
                                }.runTaskLater(instance, kickDelay);
                            } else {
                                player.sendMessage(recommendationMessage);
                            }
                        }
                    }
                }
            }
        });
    }
}
