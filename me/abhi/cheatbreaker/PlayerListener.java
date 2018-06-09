package me.abhi.cheatbreaker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;

public class PlayerListener implements PluginMessageListener, Listener{

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            String client = new String(message, "UTF-8").substring(1);
            if (!client.equals("anilla")) {
                if (API.kick) {
                    player.kickPlayer(API.kickMessage);
                } else {
                    player.sendMessage(API.recommendationMessage);
                }
            } else {
                player.sendMessage(API.welcomeMessage);
                API.cheatbreakerUsers.add(player.getUniqueId());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        API.cheatbreakerUsers.remove(player.getUniqueId());
    }
}
