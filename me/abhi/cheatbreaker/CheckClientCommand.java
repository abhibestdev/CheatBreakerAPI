package me.abhi.cheatbreaker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckClientCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("cheatbreakerapi.command.checkclient")) {
            sender.sendMessage(API.noPermissionMessage);
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <player>");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(API.couldNotFindPlayerMessage);
            return true;
        }
        if (API.cheatbreakerUsers.contains(player.getUniqueId())) {
            sender.sendMessage(API.playerIsOnCheatBreakerMessage.replace("%player%", player.getName()));
            return true;
        }
        sender.sendMessage(API.playerIsNotOnCheatBreakerMessage.replace("%player%", player.getName()));
        return true;
    }
}
