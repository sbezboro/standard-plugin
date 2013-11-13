package com.sbezboro.standardplugin.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.managers.HoneypotManager;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;

public class HoneypotCommand extends BaseCommand {

	public HoneypotCommand(StandardPlugin plugin) {
		super(plugin, "honeypot");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			showUsageInfo(sender);
			return false;
		}
		
		HoneypotManager honeypotManager = plugin.getHoneypotManager();

		if (args[0].equalsIgnoreCase("generate")) {
			
			if (args.length == 1) {
				honeypotManager.createHoneypot(null);
			} else if (args.length == 3) {
				if (args[1].equals("under")) {
					World overworld = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME);
					
					StandardPlayer player = plugin.getStandardPlayer(sender);
					
					if (player == null) {
						showPlayerOnlyMessage(sender);
						return false;
					}

					try {
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY() - Integer.parseInt(args[2]);
						int z = player.getLocation().getBlockZ();

						Location location = new Location(overworld, x, y, z);
						honeypotManager.createHoneypot(location);
						
						sender.sendMessage("Honeypot generated at " + MiscUtil.locationFormat(location));
					} catch (NumberFormatException e) {
						sender.sendMessage("Error parsing coordinates");
						return false;
					}
				} else {
					showUsageInfo(sender);
					return false;
				}
			} else if (args.length == 4) {
				World overworld = plugin.getServer().getWorld(StandardPlugin.OVERWORLD_NAME);
				
				try {
					int x = Integer.parseInt(args[1]);
					int y = Integer.parseInt(args[2]);
					int z = Integer.parseInt(args[3]);
					
					Location location = new Location(overworld, x, y, z);
					honeypotManager.createHoneypot(location);
					
					sender.sendMessage("Honeypot generated at " + MiscUtil.locationFormat(location));
				} catch (NumberFormatException e) {
					sender.sendMessage("Error parsing coordinates");
					return false;
				}
			} else {
				showUsageInfo(sender);
				return false;
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			Location location = honeypotManager.removeOldestHoneypot();
			
			if (location == null) {
				sender.sendMessage("No honeypots left to remove");
			} else {
				sender.sendMessage("Honeypot removed at " + MiscUtil.locationFormat(location));
			}
		}
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name + " generate [x y z]");
		sender.sendMessage("Usage: /" + name + " generate under <below>");
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
