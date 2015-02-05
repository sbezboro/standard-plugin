package com.sbezboro.standardplugin.commands;

import com.sbezboro.http.HttpRequestManager;
import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.UsernamesHttpRequest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UsernamesCommand extends BaseCommand {

	public UsernamesCommand(StandardPlugin plugin) {
		super(plugin, "usernames");
	}

	@Override
	public boolean handle(final CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			showUsageInfo(sender);
			return false;
		}

		final StandardPlayer senderPlayer = plugin.getStandardPlayer(sender);
		final StandardPlayer player;

		if (args.length > 0) {
			player = plugin.matchPlayer(args[0]);
		} else {
			player = senderPlayer;
		}

		if (player == null) {
			sender.sendMessage("Player " + args[0] + " not found!");
			return true;
		}

		HttpRequestManager.getInstance().startRequest(
			new UsernamesHttpRequest(player, new HttpRequestListener() {

			@Override
			public void requestSuccess(HttpResponse response) {
				String displayName;
				if (player.hasNickname()) {
					displayName = ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + " (" + player.getName() + ")";
				} else {
					displayName = ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET;
				}

				List<Object> usernames = response.getList("past_usernames");

				if (usernames.isEmpty()) {
					sender.sendMessage(displayName + " has no other known usernames");
				} else {
					sender.sendMessage(displayName + " has had the following usernames:");

					for (Object otherUsername : usernames) {
						sender.sendMessage(ChatColor.AQUA + (String)otherUsername);
					}
				}
			}

			@Override
			public void requestFailure(HttpResponse response) {
				sender.sendMessage(ChatColor.RED + "There was a problem getting usernames");
			}
		}));

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
