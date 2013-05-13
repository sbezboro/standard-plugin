package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.http.HttpResponse;
import com.sbezboro.http.listeners.HttpRequestListener;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.net.RankHttpRequest;
import com.sbezboro.standardplugin.util.MiscUtil;

public class RankCommand extends BaseCommand {

	public RankCommand(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean handle(final CommandSender sender, Command command, String label, final String[] args) {
		final StandardPlayer senderPlayer = plugin.getStandardPlayer(sender);

		if (args.length > 1) {
			showUsageInfo(sender);
			return false;
		}

		final String username;
		final StandardPlayer rankPlayer;
		if (args.length == 1) {
			rankPlayer = plugin.getStandardPlayer(args[0]);
		} else {
			rankPlayer = senderPlayer;
		}
		
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
					
					if (senderPlayer != null && rankPlayer == senderPlayer) {
						sender.sendMessage("You are ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!");
						sender.sendMessage("You have played here " + ChatColor.AQUA + time + ChatColor.WHITE + ".");
					} else {
						sender.sendMessage(actualUsername + " is ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!");
						sender.sendMessage(actualUsername + " has played here " + ChatColor.AQUA + time + ChatColor.WHITE + ".");
					}
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
		sender.sendMessage("Usage: /" + getName() + " [username]");
		sender.sendMessage(ChatColor.GREEN + "This command will tell you the rank of either yourself");
		sender.sendMessage(ChatColor.GREEN + "or a player you specify. ");
	}

	@Override
	public String getName() {
		return "rank";
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return numArgs == 0;
	}
}
