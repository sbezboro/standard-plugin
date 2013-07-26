package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PvpProtectionCommand extends BaseCommand {

	public PvpProtectionCommand(StandardPlugin plugin) {
		super(plugin, "pvp");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0 || args.length > 2) {
			showUsageInfo(sender);
			return false;
		}

		StandardPlayer player = plugin.matchPlayer(args[0]);
		if (args.length == 1) {
			sender.sendMessage("PVP protection is " + (player.isPvpProtected() ? "enabled" : "disabled") + " for " + player.getDisplayName(false));
		} else if (args.length == 2) {
			if (!args[1].equals("on") && !args[1].equals("off")) {
				showUsageInfo(sender);
				return false;
			}

			boolean enabled = args[1].equals("on");
			player.setPvpProtection(enabled);

			if (enabled) {
				sender.sendMessage("Enabled PVP protection for " + player.getDisplayName(false) + "!");
			} else {
				sender.sendMessage("Disabled PVP protection for " + player.getDisplayName(false) + "!");
			}
		}

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " <name> <on/off>");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
