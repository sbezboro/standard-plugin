package com.sbezboro.standardplugin.model;

import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.persistence.PlayerStorage;

public class StandardPlayer extends PlayerDelegate {
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public void loadProperties() {
		// Nothing yet
	}
	
	public Player getBase() {
		return base;
	}
}
