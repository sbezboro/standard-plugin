package com.sbezboro.standardplugin.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

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
			Long minutes = (Long) playerData.get("minutes");
			
			StandardPlayer player = plugin.getStandardPlayer(username);
			
			player.setTimeSpent(minutes.intValue());
			
			if (minutes % 6000 == 0) {
				String time = String.valueOf(minutes / 60) + " hours";
				Bukkit.broadcastMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.BLUE + " has just reached " + ChatColor.AQUA + time + ChatColor.BLUE + " on the server! Congrats!");
			}
		}
		
		
		return true;
	}
}
