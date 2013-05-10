package com.sbezboro.standardplugin.model;

import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.persistence.PlayerStorage;

public class StandardPlayer extends PlayerDelegate {
	private String forumMuted;
	
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public void loadProperties() {
		forumMuted = (String) loadProperty("nickname");
	}
	
	public String isForumMuted() {
		return forumMuted;
	}

	public void setForumMuted(boolean forumMuted) {
		saveProperty("forumMuted", forumMuted);
	}
	
	public Player getBase() {
		return base;
	}
}
