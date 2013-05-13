package com.sbezboro.standardplugin.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.persistence.PlayerStorage;

public class StandardPlayer extends PlayerDelegate {
	private static final String FORUM_MUTED_PROPERTY = "forum-muted";
	
	private boolean forumMuted;
	private Location bedLocation;
	
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public StandardPlayer(final OfflinePlayer player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	@Override
	public void loadProperties() {
		forumMuted = loadBoolean(FORUM_MUTED_PROPERTY);
	}
	
	public Boolean isForumMuted() {
		return forumMuted;
	}

	public void setForumMuted(boolean forumMuted) {
		this.forumMuted = forumMuted;
		saveProperty(FORUM_MUTED_PROPERTY, forumMuted);
	}

	public boolean toggleForumMute() {
		setForumMuted(!isForumMuted());
		return isForumMuted();
	}
	
	public void saveBedLocation(Location location) {
		bedLocation = location;
	}
	
	public Location getBedLocation() {
		return bedLocation;
	}
	
	public boolean hasNickname() {
		return !getName().equals(getDisplayName(false));
	}
	
	public void leftServer() {
		player = null;
	}
	
	public String getDisplayName(boolean colored) {
		if (isOnline()) {
			String name = super.getDisplayName();
			
			if (!colored) {
				return ChatColor.stripColor(name);
			}
			
			return name;
		}
		
		return getName();
	}

	@Override
	public String getDisplayName() {
		return getDisplayName(true);
	}
}
