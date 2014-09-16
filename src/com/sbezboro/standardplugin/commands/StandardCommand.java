package com.sbezboro.standardplugin.commands;

import com.sbezboro.standardplugin.tasks.UUIDMigrationTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;

public class StandardCommand extends BaseCommand {

	public StandardCommand(StandardPlugin plugin) {
		super(plugin, "standard");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			showUsageInfo(sender);
			return false;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			plugin.reloadPlugin();
			sender.sendMessage("Plugin reloaded");
			return true;
		} else if (args[0].equalsIgnoreCase("migrate")) {
			UUIDMigrationTask task = new UUIDMigrationTask(plugin);
			task.runTaskAsynchronously(plugin);
			return true;
		}

		showUsageInfo(sender);
		return false;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " reload");
		sender.sendMessage("Usage: /" + name + " migrate");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
