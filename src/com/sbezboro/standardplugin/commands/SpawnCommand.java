package com.sbezboro.standardplugin.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.Gate;

public class SpawnCommand extends BaseCommand {

	public SpawnCommand(StandardPlugin plugin) {
		super(plugin, "spawn");
	}

	@Override
	public boolean handle(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.YELLOW + "Notable areas:");

		List<Gate> gateList = new ArrayList<Gate>();
		for (Gate gate : plugin.getGateStorage().getGates()) {
			if (gate.getDisplayName() != null) {
				gateList.add(gate);
			}
		}

		Collections.sort(gateList, new Comparator<Gate>() {

			@Override
			public int compare(Gate gate1, Gate gate2) {
				return gate1.getDisplayName().compareTo(gate2.getDisplayName());
			}
		});

		for (Gate gate : gateList) {
			sender.sendMessage(ChatColor.AQUA + gate.getDisplayName() + ChatColor.WHITE + " (" + gate.getLocation().getBlockX() + ", "
					+ gate.getLocation().getBlockY() + ", " + gate.getLocation().getBlockZ() + ")");
		}

		if (gateList.size() == 0) {
			sender.sendMessage("None");
		}

		return true;
	}

	@Override
	public void showUsageInfo(CommandSender sender) {
		sender.sendMessage("Usage: /" + name);
	}

	@Override
	public boolean isPlayerOnly(int numArgs) {
		return false;
	}

}
