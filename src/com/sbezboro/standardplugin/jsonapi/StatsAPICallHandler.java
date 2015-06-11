package com.sbezboro.standardplugin.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sbezboro.standardplugin.model.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;

public class StatsAPICallHandler extends APICallHandler {
	private boolean lastSession = true;

	public StatsAPICallHandler(StandardPlugin plugin) {
		super(plugin, "stats");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject handle(HashMap<String, Object> payload) {
		if (payload == null) {
			return okResult();
		}

		ArrayList<HashMap<String, Object>> players = (ArrayList<HashMap<String, Object>>) payload.get("player_stats");
		for (HashMap<String, Object> playerData : players) {
			String uuid = (String) playerData.get("uuid");
			String username = (String) playerData.get("username");
			Long timeSpent = (Long) playerData.get("minutes");
			Long rank = (Long) playerData.get("rank");
			ArrayList<HashMap<String, Object>> titles = (ArrayList<HashMap<String, Object>>) playerData.get("titles");

			StandardPlayer player = plugin.getStandardPlayerByUUID(uuid);

			if (!player.isOnline()) {
				continue;
			}

			if (!player.getUuidString().equals(uuid)) {
				plugin.getLogger().severe("UUID mismatch! " + username + "'s uuid is " + player.getUuidString() + ", expected " + uuid);
				continue;
			}

			player.setTimeSpent(timeSpent.intValue());
			player.setRank(rank.intValue());

			plugin.getTitleStorage().addTitles(titles, player);

			long hours = timeSpent / 60;
			String hoursString = String.valueOf(hours) + MiscUtil.pluralize(" hour", hours);
			if (timeSpent % 6000 == 0) {
				StandardPlugin.broadcast(ChatColor.AQUA + player.getDisplayName() + ChatColor.BLUE + " has just reached " + ChatColor.AQUA + hoursString
						+ ChatColor.BLUE + " on the server! Congrats!");
			}

			if (plugin.isPvpProtectionEnabled()) {
				// Various new player messages
				if (timeSpent == 1) {
					player.sendMessage(ChatColor.DARK_AQUA + "Welcome to " + ChatColor.BOLD + "Standard Survival" + ChatColor.DARK_AQUA
							+ "! To make it easier for you to settle in, you start off with:");
					player.sendMessage("" + ChatColor.GOLD + plugin.getPvpProtectionTime() + " minutes of PVP protection");
					player.sendMessage("" + ChatColor.GOLD + plugin.getHungerProtectionTime() + " minutes of hunger protection.");
				} else if (timeSpent == 2) {
					player.sendMessage(ChatColor.DARK_AQUA + "Try to travel far away from spawn to find a suitable spot in the wilderness to begin your adventure. "
							+ "There are a lot of groups, both hostile and friendly, in the area surrounding spawn.");
				} else if (timeSpent == 3) {
					player.sendMessage(ChatColor.DARK_AQUA
							+ "Attacking another player will automatically disable your PVP protection, so be careful! Good luck and have fun!");
				}

				if (player.isPvpProtected()) {
					int remainingTime = player.getPvpProtectionTimeRemaining();

					// Disable PVP protection after time is up
					if (remainingTime == 0) {
						player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "After playing on the server for " + ChatColor.BOLD + ChatColor.AQUA + hoursString
								+ ChatColor.RED + ChatColor.BOLD + " your PVP protection has been disabled! Stay safe out there!");

						player.setPvpProtection(false);
						logger.info("Disabling PVP protection for " + player.getName() + " after reaching time limit.");
						// Show warning a few times before the PVP protection wears off
					} else if (timeSpent % (plugin.getPvpProtectionTime() / 4) == 0 || remainingTime == 5) {
						player.sendMessage(ChatColor.RED + "Warning! You have " + ChatColor.AQUA + remainingTime + ChatColor.RED
								+ MiscUtil.pluralize(" minute", remainingTime) + " left until PVP protection is disabled!");
					}
				}
			}
		}
		
		boolean session = (Boolean) payload.get("session");
		
		if (!session) {
			StandardPlugin.broadcast(ChatColor.DARK_AQUA + "Session servers are down! If you leave the server now, you probably won't be able to get back on!", false, true);
		} else if (!lastSession) {
			StandardPlugin.broadcast(ChatColor.DARK_AQUA + "Session servers appear to have come back up.", false, true);
		}
		
		lastSession = session;

		return okResult();
	}
}
