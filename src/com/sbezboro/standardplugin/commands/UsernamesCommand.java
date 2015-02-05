package com.sbezboro.standardplugin.commands;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UsernamesCommand extends BaseCommand {

	public UsernamesCommand(StandardPlugin plugin) {
		super(plugin, "usernames");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			showUsageInfo(sender);
			return false;
		}

		final StandardPlayer senderPlayer = plugin.getStandardPlayer(sender);

		final String username;
		if (args.length > 0) {
			username = args[0];
		} else {
			username = senderPlayer.getName();
		}

		StandardPlayer player = plugin.matchPlayer(username);

		if (player == null) {
			sender.sendMessage("Player " + username + " not found!");
			return true;
		}

		List<String> usernames = player.getPastUsernames();

		if (usernames.isEmpty()) {
			sender.sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + " (" + player.getName() + ") has no other known usernames");
		} else {
			sender.sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + " (" + player.getName() + ") has had the following usernames:");

			for (String otherUsername : usernames) {
				sender.sendMessage(ChatColor.AQUA + otherUsername);
			}
		}

		player.teleport(player);
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " [username]");
		sender.sendMessage(ChatColor.GREEN + "This command will tell you the past known usernames of a player");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return numArgs == 0;
	}
}
