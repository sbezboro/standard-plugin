package com.sbezboro.standardplugin.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.persistence.PersistedProperty;
import com.sbezboro.standardplugin.persistence.PlayerStorage;
import com.sbezboro.standardplugin.util.MiscUtil;

public class StandardPlayer extends PlayerDelegate {
	private static final String FORUM_MUTED_PROPERTY = "forum-muted";
	private static final String PVP_PROTECTION_PROPERTY = "pvp-protection";
	
	private PersistedProperty<Boolean> forumMuted;
	private PersistedProperty<Boolean> pvpProtection;
	
	private Location bedLocation;
	private int timeSpent;
	
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public StandardPlayer(final OfflinePlayer player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	@Override
	public void loadProperties() {
		forumMuted = loadProperty(Boolean.class, FORUM_MUTED_PROPERTY);
		pvpProtection = loadProperty(Boolean.class, PVP_PROTECTION_PROPERTY);
	}
	
	public Boolean isForumMuted() {
		return forumMuted.getValue();
	}

	public void setForumMuted(boolean forumMuted) {
		this.forumMuted.setValue(forumMuted);
	}

	public boolean toggleForumMute() {
		setForumMuted(!isForumMuted());
		return isForumMuted();
	}
	
	public int getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public Boolean isPvpProtected() {
		return pvpProtection.getValue();
	}

	public void setPvpProtection(boolean pvpProtection) {
		this.pvpProtection.setValue(pvpProtection);
	}
	
	public int getPvpProtectionTimeRemaining() {
		return Math.max(StandardPlugin.getPlugin().getPvpProtectionTime() - timeSpent, 0);
	}
	
	public void saveBedLocation(Location location) {
		bedLocation = location;
	}
	
	public Location getBedLocation() {
		return bedLocation;
	}
	
	public void leftServer() {
		player = null;
	}
	
	public String getRankDescription(boolean self, int rank, boolean veteran) {
		String message = "";
		if (self) {
			if (veteran) {
				message += "You are a " + ChatColor.GOLD + ChatColor.BOLD + "veteran" + ChatColor.WHITE + " and ";
			} else {
				message = "You ";
			}
			
			message += "are ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!";
		} else {
			if (veteran) {
				message += "" + ChatColor.GOLD + ChatColor.BOLD + "Veteran ";
			}
			
			message += ChatColor.AQUA + getDisplayName() + ChatColor.WHITE + " is ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!";
		}
		return message;
	}
	
	public String getTimePlayedDescription(boolean self, String time) {
		if (self) {
			return "You have played here " + ChatColor.AQUA + time + ChatColor.WHITE + ".";
		} else {
			return getName() + " has played here " + ChatColor.AQUA + time + ChatColor.WHITE + ".";
		}
	}
	
	public boolean hasNickname() {
		return EssentialsIntegration.hasNickname(getName());
	}
	
	public String getDisplayName(boolean colored) {
		if (hasNickname()) {
			String name = EssentialsIntegration.getNickname(getName());
			
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
