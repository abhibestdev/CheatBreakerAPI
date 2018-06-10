package bungee.me.abhi.cheatbreaker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class API extends Plugin implements Listener{

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
        try {
			load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cheatbreakerUsers = new ArrayList<>();
        register();
    }

    @Override
    public void onDisable() {
        for (ProxiedPlayer all : this.getProxy().getPlayers()) {
            all.disconnect(new TextComponent(serverRestartMessage));
        }
    }

    private void load() throws IOException {    	
    	if(this.getDataFolder().exists())
    		this.getDataFolder().mkdir();
    	
    	File f = new File(getDataFolder(), "config.yml");
    	if(!f.exists()) {
    		f.createNewFile();
    		 try (InputStream in = getResourceAsStream("config.yml")) {
                 Files.copy(in, f.toPath());
             } catch (IOException e) {
                 e.printStackTrace();
             }
    	}
    	Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
        kick = cfg.getBoolean("kick");
        kickDelay = cfg.getInt("kick-delay");
        serverRestartMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("server-restart"));
        recommendationMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("recommendation-message"));
        kickMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("kick-message"));
        welcomeMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("welcome-message"));
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("no-permission"));
        couldNotFindPlayerMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("could-not-find-player"));
        playerIsOnCheatBreakerMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("player-is-on-cheatbreaker"));
        playerIsNotOnCheatBreakerMessage = ChatColor.translateAlternateColorCodes('&', cfg.getString("player-is-not-on-cheatbreaker"));
    }

    private void register() {
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
        getProxy().getPluginManager().registerCommand(this, new CheckClientCommand());
    }

    @EventHandler
    public void onPacket(PluginMessageEvent e) {    	
    	if(e.getSender() instanceof ProxiedPlayer && e.getTag() != null) {
    		String channel = e.getTag();
    		ProxiedPlayer player = (ProxiedPlayer)e.getSender();
        	boolean cheatbreaker = false;
    		if (channel.equals("CB|INIT") || channel.equals("CB-Binary")) {
                cheatbreaker = true;
            }
            if (!cheatbreakerUsers.contains(player.getUniqueId())) {
                if (cheatbreaker) {
                    player.sendMessage(new TextComponent(welcomeMessage));
                    cheatbreakerUsers.add(player.getUniqueId());
                } else {
                    if (kick) {
                    	this.getProxy().getScheduler().schedule(this, new Runnable() {
                            public void run() {
                            	player.disconnect(new TextComponent(kickMessage));
                            }
                         }, kickDelay/20, TimeUnit.SECONDS);
                    } else {
                        player.sendMessage(new TextComponent(recommendationMessage));
                    }
                }
            }	
    	}    	
    }   
}
