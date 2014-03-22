package com.sbezboro.standardplugin.commands;

import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;

public abstract class SubCommand {
	protected StandardPlugin plugin;
	protected BaseCommand command;
	protected String commandName;
	
	public SubCommand(StandardPlugin plugin, BaseCommand command, String commandName) {
		this.plugin = plugin;
		this.command = command;
		this.commandName = commandName;
	}
	
	public abstract boolean handle(CommandSender sender, String[] args);
	
	public abstract void showHelp(CommandSender sender);

	public String getCommandName() {
		return commandName;
	}
}
