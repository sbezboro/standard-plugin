package com.sbezboro.standardplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;

public class EndresetCommand extends BaseCommand {

	public EndresetCommand(StandardPlugin plugin) {
		super(plugin, "endreset");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			showUsageInfo(sender);
			return false;
		}
		
		if (plugin.getEndResetManager().isEndResetScheduled()) {
			long endReset = plugin.getEndResetStorage().getNextReset();
			sender.sendMessage(ChatColor.BLUE + "The next end reset is on " + ChatColor.AQUA + MiscUtil.friendlyTimestamp(endReset));
		} else {
			sender.sendMessage(ChatColor.BLUE + "No end reset scheduled! The ender dragon is still alive!");
		}
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name);
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
