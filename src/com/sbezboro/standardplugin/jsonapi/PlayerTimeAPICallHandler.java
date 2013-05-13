package com.sbezboro.standardplugin.jsonapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerTimeAPICallHandler extends APICallHandler {

	public PlayerTimeAPICallHandler(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public Object handle(Object[] args) {
		if (args.length != 2) {
			return false;
		}
		
		String username = (String) args[0];
		Long minutes = (Long) args[1];
		
		StandardPlayer player = plugin.getStandardPlayer(username);
		
		String time = String.valueOf(minutes / 60) + " hours";
		
		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getDisplayName(false) + ChatColor.BLUE + " has just reached " + ChatColor.AQUA + time + ChatColor.BLUE + " on the server! Congrats!");
		
		return true;
	}

	@Override
	public String getName() {
		return "player_time";
	}
}
