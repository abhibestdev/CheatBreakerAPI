package me.abhi.cheatbreaker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CheckClientCommand extends BukkitCommand {

    public CheckClientCommand() {
        super("checkclient");
        this.setPermission("cheatbreakerapi.command.checkclient");
        this.setPermissionMessage(API.noPermissionMessage);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(this.getPermission())) {
            sender.sendMessage(this.getPermissionMessage());
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
