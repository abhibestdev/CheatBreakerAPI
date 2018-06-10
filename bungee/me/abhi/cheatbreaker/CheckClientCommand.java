package bungee.me.abhi.cheatbreaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CheckClientCommand extends Command {

	public CheckClientCommand() {
		super("checkclient");
	}	

	@Override
	public void execute(CommandSender s, String[] args) {
		if (!s.hasPermission("cheatbreakerapi.command.checkclient")) {
            s.sendMessage(new TextComponent(API.noPermissionMessage));
            return;
        }
        if (args.length != 1) {
            s.sendMessage(new TextComponent(ChatColor.RED + "Usage: /checkclient <player>"));
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            s.sendMessage(new TextComponent(API.couldNotFindPlayerMessage));
            return;
        }
        if (API.cheatbreakerUsers.contains(player.getUniqueId())) {
            s.sendMessage(new TextComponent(API.playerIsOnCheatBreakerMessage.replace("%player%", player.getName())));
            return;
        }
        s.sendMessage(new TextComponent(API.playerIsNotOnCheatBreakerMessage.replace("%player%", player.getName())));
	}
}
