package com.sbezboro.standardplugin.commands;

import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand implements Comparable<SubCommand> {
	protected StandardPlugin plugin;
	protected BaseCommand command;
	protected String commandName;
	protected List<String> alternatives;
	
	public SubCommand(StandardPlugin plugin, BaseCommand command, String commandName) {
		this.plugin = plugin;
		this.command = command;
		this.commandName = commandName;
		this.alternatives = new ArrayList<String>();
	}

	public SubCommand(StandardPlugin plugin, BaseCommand command, String commandName, List<String> alternatives) {
		this.plugin = plugin;
		this.command = command;
		this.commandName = commandName;
		this.alternatives = alternatives;
	}
	
	public abstract boolean handle(CommandSender sender, String[] args);
	
	public abstract void showHelp(CommandSender sender);

	public String getCommandName() {
		return commandName;
	}

	public List<String> getAlternatives() {
		return alternatives;
	}

	@Override
	public int compareTo(SubCommand other) {
		return this.commandName.compareTo(other.getCommandName());
	}
}
