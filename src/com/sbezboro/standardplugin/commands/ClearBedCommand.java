package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ClearBedCommand extends BaseCommand {

	public ClearBedCommand(StandardPlugin plugin) {
		super(plugin, "clearbed");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		StandardPlayer player = plugin.getStandardPlayer(sender);
		player.saveBedLocation(null);
		player.setBedSpawnLocation(null, true);
		player.sendMessage("Your spawn has been reset!");
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name);
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return true;
	}

}
