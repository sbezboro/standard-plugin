package com.sbezboro.standardplugin.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class GateCommand extends BaseCommand {

	public GateCommand(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			showUsageInfo(sender);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			if (sender instanceof ConsoleCommandSender) {
				showPlayerOnlyMessage(sender);
				return false;
			}

			StandardPlayer player = plugin.getStandardPlayer((Player) sender);
			if (args.length == 1) {
				sender.sendMessage("Usage: /" + getName() + " create <name> [displayName]");
				return false;
			} else if (args.length == 2) {
				Gate warp = new Gate(args[1], null, player.getLocation());
				plugin.getGateStorage().addGate(warp);
				
				player.sendMessage("Gate \"" + args[1] + "\" created!");
			} else {
				String displayName = StringUtils.join(args, " ", 2, args.length);
				Gate warp = new Gate(args[1], displayName, player.getLocation());
				plugin.getGateStorage().addGate(warp);
				
				player.sendMessage("Gate \"" + args[1] + "\" (" + displayName + ") created!");
			}
		} else if (args[0].equalsIgnoreCase("delete")) {
			if (args.length == 1) {
				sender.sendMessage("Usage: /" + getName() + " delete <name>");
				return false;
			}
			
			if (args.length == 2) {
				Gate gate = plugin.getGateStorage().getGate(args[1]);
				if (gate == null) {
					sender.sendMessage("Gate \"" + args[1] + "\" does not exist.");
				} else {
					plugin.getGateStorage().removeGate(gate);
					sender.sendMessage("Gate \"" + args[1] + "\" removed!");
				}
			} else {
				sender.sendMessage("Usage: /" + getName() + " delete <name>");
				return false;
			}
		} else if (args[0].equalsIgnoreCase("link")) {
			if (args.length == 1) {
				sender.sendMessage("Usage: /" + getName() + " link <gate1> <gate2>");
				return false;
			}
			
			if (args.length == 3) {
				Gate source = plugin.getGateStorage().getGate(args[1]);
				Gate target = plugin.getGateStorage().getGate(args[2]);
				
				if (source == null) {
					sender.sendMessage("Gate \"" + args[1] + "\" does not exist.");
				} else if (target == null) {
					sender.sendMessage("Gate \"" + args[2] + "\" does not exist.");
				} else {
					plugin.getGateStorage().linkGates(source, target);
					sender.sendMessage("Gate \"" + args[1] + "\" linked to \"" + args[2] + "\"");
				}
			} else {
				sender.sendMessage("Usage: /" + getName() + " link <gate1> <gate2>");
				return false;
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			if (args.length == 1) {
				for (Gate gate : plugin.getGateStorage().getGates()) {
					String gateInfo = ChatColor.AQUA + gate.getName() + ChatColor.WHITE;
					if (gate.getDisplayName() != null) {
						gateInfo += " - " + ChatColor.YELLOW + gate.getDisplayName() + ChatColor.WHITE;
					}
					
					gateInfo += " (" + gate.getLocation().getBlockX() + ", " + gate.getLocation().getBlockY() + ", " + gate.getLocation().getBlockZ() + ")";
					
					if (gate.getTarget() != null) {
						gateInfo += " linked to " + ChatColor.AQUA + gate.getTarget().getName();
					}
					
					sender.sendMessage(gateInfo);
				}
			} else {
				sender.sendMessage("Usage: /" + getName() + " list");
				return false;
			}
		} else if (args[0].equalsIgnoreCase("tp")) {
			if (sender instanceof ConsoleCommandSender) {
				showPlayerOnlyMessage(sender);
				return false;
			}

			StandardPlayer player = plugin.getStandardPlayer((Player) sender);
			if (args.length == 2) {
				Gate gate = plugin.getGateStorage().getGate(args[1]);
				if (gate == null) {
					sender.sendMessage("Gate \"" + args[1] + "\" does not exist.");
				} else {
					player.teleport(gate.getLocation());
					sender.sendMessage("TPed to \"" + args[1] + "\"");
				}
			} else {
				sender.sendMessage("Usage: /" + getName() + " tp <name>");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + getName() + " create <name> [displayName]");
		sender.sendMessage("Usage: /" + getName() + " delete <name>");
		sender.sendMessage("Usage: /" + getName() + " link <gate1> <gate2>");
		sender.sendMessage("Usage: /" + getName() + " list");
		sender.sendMessage("Usage: /" + getName() + " tp <gate>");
	}

	@Override
	public String getName() {
		return "gate";
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
