package com.sbezboro.standardplugin.commands;

import com.sbezboro.standardplugin.util.PaginatedOutput;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand implements Comparable<SubCommand> {
	protected StandardPlugin plugin;
	protected BaseCommand command;
	protected String commandName;
	protected List<String> alternatives;

	protected List<String> helpLines;
	
	public SubCommand(StandardPlugin plugin, BaseCommand command, String commandName) {
		this.plugin = plugin;
		this.command = command;
		this.commandName = commandName;
		this.alternatives = new ArrayList<String>();

		this.helpLines = new ArrayList<String>();
	}

	public SubCommand(StandardPlugin plugin, BaseCommand command, String commandName, List<String> alternatives) {
		this.plugin = plugin;
		this.command = command;
		this.commandName = commandName;
		this.alternatives = alternatives;

		this.helpLines = new ArrayList<String>();
	}
	
	public abstract boolean handle(CommandSender sender, String[] args);
	
	public void addHelp(String line) {
		this.helpLines.add(line);
	}

	public final void showHelp(CommandSender sender) {
		for (String line : helpLines) {
			sender.sendMessage(line);
		}
	}

	public final void showHelp(PaginatedOutput output) {
		for (String line : helpLines) {
			output.addLine(line);
		}
	}

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
