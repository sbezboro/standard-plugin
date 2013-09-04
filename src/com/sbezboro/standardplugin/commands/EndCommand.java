package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndCommand extends BaseCommand {

	public EndCommand(StandardPlugin plugin) {
		super(plugin, "end");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			showUsageInfo(sender);
			return false;
		}

		if (args[0].equalsIgnoreCase("reset")) {
			plugin.getEndResetManager().resetEnd();
		} else if (args[0].equalsIgnoreCase("portals")) {
			sender.sendMessage("The following end portals are active:");
			for (Location location : plugin.getEndResetStorage().getActivePortals()) {
				sender.sendMessage(String.format("%s%s", ChatColor.DARK_AQUA, MiscUtil.locationFormat(location)));
			}
		}
		
		return false;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " reset");
		sender.sendMessage("Usage: /" + name + " portals");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
