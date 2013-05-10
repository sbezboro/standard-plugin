package com.sbezboro.standardplugin.persistence;

import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class PlayerStorage extends ObjectStorage<StandardPlayer> {
	public PlayerStorage(StandardPlugin plugin) {
		super(plugin, "players");
	}
	
	public StandardPlayer getPlayer(String username) {
		StandardPlayer standardPlayer = getObject(username);
		
		if (standardPlayer == null) {
			Player player = plugin.getServer().getPlayer(username);
			standardPlayer = new StandardPlayer(player, this);
			
			cacheObject(username, standardPlayer);
		}
		
		return standardPlayer;
	}
}
