package com.sbezboro.standardplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class SetSpawnCommand extends BaseCommand {

	public SetSpawnCommand(StandardPlugin plugin) {
		super(plugin, "setspawn");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		StandardPlayer player = plugin.getStandardPlayer(sender);
		Location location = player.getLocation();
		player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		player.sendMessage("Spawn set!");
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
