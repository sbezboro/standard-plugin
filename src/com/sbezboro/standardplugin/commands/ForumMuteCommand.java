package com.sbezboro.standardplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ForumMuteCommand extends BaseCommand {

	public ForumMuteCommand(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			List<Player> players = Bukkit.matchPlayer(args[0]);
			String username;
			
			if (players.size() == 0) {
				username = args[0];
			} else {
				username = players.get(0).getName();
			}
			
			StandardPlayer player = plugin.getStandardPlayer(username);
			boolean muted = player.toggleForumMute();
			
			if (muted) {
				sender.sendMessage(username + " forum muted!");
			} else {
				sender.sendMessage(username + " forum unmuted!");
			}
		} else {
			showUsageInfo(sender);
			return false;
		}
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + getName() + " <name>");
	}

	@Override
	public String getName() {
		return "forummute";
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
