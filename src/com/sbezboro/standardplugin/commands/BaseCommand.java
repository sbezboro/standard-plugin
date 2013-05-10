package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public abstract class BaseCommand implements CommandExecutor {
	protected StandardPlugin plugin;
	
	public BaseCommand(StandardPlugin plugin) {
		this.plugin = plugin;
		StandardPlugin.getPlugin().getCommand(getName()).setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		StandardPlayer player = null;
		if (sender instanceof Player) {
			player = plugin.getStandardPlayer((Player) sender);
		}
		
		if (player == null && isPlayerOnly(args.length)) {
			showPlayerOnlyMessage(sender);
		} else {
			return handle(sender, command, label, args);
		}
		
		return false;
	}
	
	protected void showPlayerOnlyMessage(CommandSender sender) {
		sender.sendMessage("This command can only be run by a player.");
	}
	
	public abstract boolean handle(CommandSender sender, Command command, String label, String[] args);
	public abstract void showUsageInfo(CommandSender sender);
	public abstract String getName();
	public abstract boolean isPlayerOnly(int numArgs);
}
