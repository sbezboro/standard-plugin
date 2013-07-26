package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ForumMuteCommand extends BaseCommand {

	public ForumMuteCommand(StandardPlugin plugin) {
		super(plugin, "forummute");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			StandardPlayer player = plugin.matchPlayer(args[0]);

			boolean muted = player.toggleForumMute();

			if (muted) {
				sender.sendMessage(player.getDisplayName(false) + " forum muted!");
			} else {
				sender.sendMessage(player.getDisplayName(false) + " forum unmuted!");
			}
		} else {
			showUsageInfo(sender);
			return false;
		}

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " <name>");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
