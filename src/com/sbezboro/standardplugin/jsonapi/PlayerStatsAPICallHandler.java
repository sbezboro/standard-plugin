package com.sbezboro.standardplugin.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;

public class PlayerStatsAPICallHandler extends APICallHandler {

	public PlayerStatsAPICallHandler(StandardPlugin plugin) {
		super(plugin, "player_stats");
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object handle(HashMap<String, Object> payload) {
		ArrayList<HashMap<String, Object>> players = (ArrayList<HashMap<String, Object>>) payload.get("data");
		
		for (HashMap<String, Object> playerData : players) {
			String username = (String) playerData.get("username");
			Long timeSpent = (Long) playerData.get("minutes");
			Long rank = (Long) playerData.get("rank");
			
			StandardPlayer player = plugin.getStandardPlayer(username);
			
			player.setTimeSpent(timeSpent.intValue());
			player.setRank(rank.intValue());

			long hours = timeSpent / 60;
			String hoursString = String.valueOf(hours) + MiscUtil.pluralize(" hour", hours);
			if (timeSpent % 6000 == 0) {
				Bukkit.broadcastMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.BLUE + " has just reached " + ChatColor.AQUA + hoursString + ChatColor.BLUE + " on the server! Congrats!");
			}

			if (plugin.isPvpProtectionEnabled() && player.isOnline() && player.isPvpProtected()) {
				int remainingTime = player.getPvpProtectionTimeRemaining();
				
				// Disable PVP protection after time is up
				if (remainingTime == 0) {
					player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "After playing on the server for "
						+ ChatColor.BOLD + ChatColor.AQUA + hoursString + ChatColor.RED + ChatColor.BOLD
						+ " your PVP protection has been disabled! Stay safe out there!");
					
					player.setPvpProtection(false);
					logger.info("Disabling PVP protection for " + player.getName() + " after reaching time limit.");
				// Show warning a few times before the PVP protection wears off
				} else if (timeSpent % (plugin.getPvpProtectionTime() / 4) == 0 || remainingTime == 5) {
					player.sendMessage(ChatColor.RED + "Warning! You have " + ChatColor.AQUA + remainingTime 
						+ ChatColor.RED + MiscUtil.pluralize(" minute", remainingTime) + " left until PVP protection is disabled!");
				// Various new player messages
				} else if (timeSpent == 1) {
					player.sendMessage(ChatColor.AQUA + "Welcome to " + ChatColor.BOLD + "Standard Survival" + ChatColor.AQUA + "! To make it easier for you to settle in, you start of with " 
						+ plugin.getPvpProtectionTime() + " minutes of PVP protection and " + plugin.getHungerProtectionTime() + " minutes of hunger protection.");
				} else if (timeSpent == 2) {
					player.sendMessage(ChatColor.AQUA + "Try to travel far away from spawn to find a suitable spot in the wilderness to begin your adventure. "
						+ "There are a lot of factions, both hostile and friendly, in the area surrounding spawn.");
				} else if (timeSpent == 3) {
					player.sendMessage(ChatColor.AQUA + "Attacking a vulnerable player will automatically disable your PVP protection, so be careful! Good luck and have fun!");
				}
			}
		}
		
		return true;
	}
}
