package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerTimeAPICallHandler extends APICallHandler {

	public PlayerTimeAPICallHandler(StandardPlugin plugin) {
		super(plugin, "player_time");
	}

	@Override
	public Object handle(HashMap<String, Object> payload) {
		String username = (String) payload.get("username");
		Long minutes = (Long) payload.get("minutes");
		
		StandardPlayer player = plugin.getStandardPlayer(username);
		
		String time = String.valueOf(minutes / 60) + " hours";
		
		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.BLUE + " has just reached " + ChatColor.AQUA + time + ChatColor.BLUE + " on the server! Congrats!");
		
		return true;
	}
}
