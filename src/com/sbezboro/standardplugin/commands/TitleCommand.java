package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.model.Title;

public class TitleCommand extends BaseCommand {

	public TitleCommand(StandardPlugin plugin) {
		super(plugin, "title");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			showUsageInfo(sender);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("add")) {
			if (args.length != 3) {
				showUsageInfo(sender);
				return false;
			}
			
			StandardPlayer player = plugin.matchPlayer(args[1]);
			Title title = Title.getTitle(args[2]);
			if (title == null) {
				sender.sendMessage("Title not found!");
				return true;
			}
			
			player.addTitle(title);
			
			sender.sendMessage(player.getDisplayName(false) + " now has the title " + title.getDisplayName());
			
			return true;
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (args.length != 3) {
				showUsageInfo(sender);
				return false;
			}
			
			StandardPlayer player = plugin.matchPlayer(args[1]);
			player.removeTitle(args[2]);
			
			sender.sendMessage("Title " + args[2] + " removed from " + player.getDisplayName(false));
			
			return true;
		}
		
		return false;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " add <player> <title>");
		sender.sendMessage("Usage: /" + name + " remove <player> <title>");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
