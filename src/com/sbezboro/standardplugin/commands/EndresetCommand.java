package com.sbezboro.standardplugin.commands;

import java.time.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;

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
			long daysUntilReset = (endReset - System.currentTimeMillis()) / 86400000;
			DayOfWeek dayOfWeek = ZonedDateTime.now(ZoneId.of("America/New_York")).getDayOfWeek();			
			
			if ((daysUntilReset <= 3 && dayOfWeek.getValue() >= 5) || (daysUntilReset <= 7 && dayOfWeek.getValue() < 5)) {
				sender.sendMessage(ChatColor.BLUE + "The end will reset this weekend!");
			} else if (daysUntilReset <= 10 && dayOfWeek.getValue() >= 5) {
				sender.sendMessage(ChatColor.BLUE + "The end will reset the next weekend.");
			} else {
				sender.sendMessage(ChatColor.BLUE + "The end will reset the weekend after the next.");
			}
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
