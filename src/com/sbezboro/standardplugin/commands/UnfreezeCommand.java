package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class UnfreezeCommand extends BaseCommand {

	public UnfreezeCommand(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		StandardPlayer player = plugin.getStandardPlayer(sender);
		player.teleport(player);
		player.sendMessage("You should be unfrozen!");
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + getName());
	}

	@Override
	public String getName() {
		return "unfreeze";
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return true;
	}
}
