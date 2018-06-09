package me.abhi.cheatbreaker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener{

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (API.cheatbreakerUsers.contains(player.getUniqueId())) {
            API.cheatbreakerUsers.remove(player.getUniqueId());
        }
    }
}
