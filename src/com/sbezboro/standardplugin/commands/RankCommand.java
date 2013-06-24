package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.RankHttpRequest;

public class RankCommand extends BaseCommand {

	public RankCommand(StandardPlugin plugin) {
		super(plugin, "rank");
	}

	@Override
	public boolean handle(final CommandSender sender, Command command, String label, final String[] args) {
		if (args.length > 1) {
			showUsageInfo(sender);
			return false;
		}
		
		final StandardPlayer senderPlayer = plugin.getStandardPlayer(sender);
		final StandardPlayer rankPlayer;
		if (args.length == 1) {
			rankPlayer = plugin.getStandardPlayer(args[0]);
		} else {
			rankPlayer = senderPlayer;
		}

		final String username;
		if (rankPlayer == null) {
			username = args[0];
		} else {
			username = rankPlayer.getName();
		}
		
		RankHttpRequest request = new RankHttpRequest(username, false);
		request.start(new HttpRequestListener() {
			
			@Override
			public void requestSuccess(HttpResponse response) {
				int result = response.getInt("result");
				
				if (result == 1) {
					int rank = response.getInt("rank");
					String time = response.getString("time");
					String actualUsername = response.getString("username");
					
					StandardPlayer actualPlayer = plugin.getStandardPlayer(actualUsername);
					
					sender.sendMessage(actualPlayer.getRankDescription(actualPlayer == senderPlayer, rank));
					sender.sendMessage(actualPlayer.getTimePlayedDescription(actualPlayer == senderPlayer, time));
				} else {
					sender.sendMessage("The player \"" + username + "\" doesn't exist on the server.");
				}
			}
			
			@Override
			public void requestFailure(HttpResponse response) {
				sender.sendMessage("There was a problem getting the rank!");
				
				String result = response.getStringResponse();
				plugin.getLogger().severe(result);
			}
		});
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " [username]");
		sender.sendMessage(ChatColor.GREEN + "This command will tell you the rank of either yourself");
		sender.sendMessage(ChatColor.GREEN + "or a player you specify. ");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return numArgs == 0;
	}
}
