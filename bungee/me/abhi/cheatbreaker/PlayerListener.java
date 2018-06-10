package bungee.me.abhi.cheatbreaker;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (API.cheatbreakerUsers.contains(player.getUniqueId())) {
            API.cheatbreakerUsers.remove(player.getUniqueId());
        }
    }
}