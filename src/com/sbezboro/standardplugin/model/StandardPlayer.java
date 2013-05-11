package com.sbezboro.standardplugin.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.persistence.PlayerStorage;

public class StandardPlayer extends PlayerDelegate {
	private static final String FORUM_MUTED_PROPERTY = "forum-muted";
	
	private String forumMuted;
	private Location bedLocation;
	
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public void loadProperties() {
		forumMuted = (String) loadProperty(FORUM_MUTED_PROPERTY);
	}
	
	public String isForumMuted() {
		return forumMuted;
	}

	public void setForumMuted(boolean forumMuted) {
		saveProperty(FORUM_MUTED_PROPERTY, forumMuted);
	}
	
	public void saveBedLocation(Location location) {
		bedLocation = location;
	}
	
	public Location getBedLocation() {
		return bedLocation;
	}
	
	public Player getBase() {
		return base;
	}
}
