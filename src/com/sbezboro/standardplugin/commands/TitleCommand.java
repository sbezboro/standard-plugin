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
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage("The following titles exist:");
				for (Title title : plugin.getTitleStorage().getTitles()) {
					sender.sendMessage(title.getDescription());
				}
			} else {
				showUsageInfo(sender);
				return false;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("create")) {
				Title title = new Title(args[1], args[2]);
				plugin.getTitleStorage().saveTitle(title);
				
				sender.sendMessage("Title " + args[1] + " created!");
			} else if (args[0].equalsIgnoreCase("add")) {
				StandardPlayer player = plugin.matchPlayer(args[1]);
				String titleName = args[2];
				
				Title title = plugin.getTitleStorage().getTitle(titleName);
				if (title == null) {
					sender.sendMessage("Title doesn't exist!");
					return true;
				}

				if (player.hasTitle(titleName)) {
					sender.sendMessage(player.getDisplayName(false) + " already has the title " + titleName + "!");
				} else {
					player.addTitle(title);
					
					sender.sendMessage(player.getDisplayName(false) + " now has the title " + title.getDisplayName());
				}
				
				return true;
			} else if (args[0].equalsIgnoreCase("remove")) {
				StandardPlayer player = plugin.matchPlayer(args[1]);
				String titleName = args[2];
				
				if (player.hasTitle(titleName)) {
					player.removeTitle(titleName);
					
					sender.sendMessage("Title " + args[2] + " removed from " + player.getDisplayName(false));
				} else {
					sender.sendMessage(player.getDisplayName(false) + " doesn't have the title " + titleName + "!");
				}
				
				return true;
			} else {
				showUsageInfo(sender);
				return false;
			}
		} else {
			showUsageInfo(sender);
			return false;
		}
		
		return false;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " create <name> <displayName>");
		sender.sendMessage("Usage: /" + name + " add <player> <title>");
		sender.sendMessage("Usage: /" + name + " remove <player> <title>");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
