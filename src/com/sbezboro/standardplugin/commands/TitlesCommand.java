package com.sbezboro.standardplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class TitlesCommand extends BaseCommand {

	public TitlesCommand(StandardPlugin plugin) {
		super(plugin, "titles");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			showUsageInfo(sender);
			return false;
		}

		StandardPlayer senderPlayer = plugin.getStandardPlayer(sender);

		if (args.length == 0) {
			senderPlayer.sendMessage(senderPlayer.getTitleDescription(true));
		} else {
			StandardPlayer player = plugin.matchPlayer(args[0]);
			if (player.hasPlayedBefore()) {
				sender.sendMessage(player.getTitleDescription(senderPlayer == player));
			} else {
				sender.sendMessage("The player \"" + player.getName() + "\" doesn't exist on the server.");
			}
		}

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " [player]");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return numArgs == 0;
	}

}
