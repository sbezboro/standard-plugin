package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

import net.md_5.bungee.api.ChatColor;

public class EnablePvpCommand extends BaseCommand {

	public EnablePvpCommand(StandardPlugin plugin) {
		super(plugin, "enablepvp");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			showUsageInfo(sender);
			return false;
		}

		StandardPlayer player = plugin.getStandardPlayer(sender);
		
		if (player == null) {
			showPlayerOnlyMessage(sender);
			return true;
		}
		
		if (player.isPvpProtected()) {
			player.setPvpProtection(false);
			
			sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "You have disabled your PVP protection. Stay safe out there!");
		} else {
			sender.sendMessage("Your PVP protection already is disabled.");
		}

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /enablepvp");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return true;
	}
}
