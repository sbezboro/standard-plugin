package com.sbezboro.standardplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PvpProtectionCommand extends BaseCommand {

	public PvpProtectionCommand(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0 || args.length > 2) {
			showUsageInfo(sender);
			return false;
		}

		List<Player> players = Bukkit.matchPlayer(args[0]);
		
		String username;
		
		if (players.size() == 0) {
			username = args[0];
		} else {
			username = players.get(0).getName();
		}
		
		StandardPlayer player = plugin.getStandardPlayer(username);
		if (args.length == 1) {
			sender.sendMessage("PVP protection is " + (player.isPvpProtected() ? "enabled" : "disabled") + " for " + username);
		} else if (args.length == 2) {
			if (!args[1].equals("on") && !args[1].equals("off")) {
				showUsageInfo(sender);
				return false;
			}
			
			boolean enabled = args[1].equals("on");
			player.setPvpProtection(enabled);
			
			if (enabled) {
				sender.sendMessage("Enabled PVP protection for " + username + "!");
			} else {
				sender.sendMessage("Disabled PVP protection for " + username + "!");
			}
		}
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + getName() + " <name> <on/off>");
	}

	@Override
	public String getName() {
		return "pvp";
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
