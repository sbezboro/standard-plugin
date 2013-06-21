package com.sbezboro.standardplugin.model;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.persistence.PersistedList;
import com.sbezboro.standardplugin.persistence.PersistedProperty;
import com.sbezboro.standardplugin.persistence.PlayerStorage;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;
import com.sbezboro.standardplugin.util.MiscUtil;

public class StandardPlayer extends PlayerDelegate {
	private static final String FORUM_MUTED_PROPERTY = "forum-muted";
	private static final String PVP_PROTECTION_PROPERTY = "pvp-protection";
	private static final String BED_LOCATION_PROPERTY = "bed";
	private static final String TITLES_PROPERTY = "titles";
	
	private PersistedProperty<Boolean> forumMuted;
	private PersistedProperty<Boolean> pvpProtection;
	private PersistedProperty<PersistableLocation> bedLocation;
	private PersistedList<String> titleNames;
	
	private ArrayList<Title> titles;
	
	private int timeSpent;
	private int rank;
	
	private int newbieAttacks;
	
	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	public StandardPlayer(final OfflinePlayer player, final PlayerStorage storage) {
		super(player, storage);
	}
	
	@Override
	public void loadProperties() {
		titles = new ArrayList<Title>();
		
		forumMuted = loadProperty(Boolean.class, FORUM_MUTED_PROPERTY);
		pvpProtection = loadProperty(Boolean.class, PVP_PROTECTION_PROPERTY);
		bedLocation = loadProperty(PersistableLocation.class, BED_LOCATION_PROPERTY);
		titleNames = loadList(String.class, TITLES_PROPERTY);

		for (String name : titleNames) {
			Title title = Title.getTitle(name);
			titles.add(title);
		}
	}
	
	// ------
	// Persisted property mutators
	// ------
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

	public boolean isHungerProtected() {
		return timeSpent <= StandardPlugin.getPlugin().getHungerProtectionTime();
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
		bedLocation.setValue(new PersistableLocation(location));
	}
	
	public Location getBedLocation() {
		return bedLocation.getValue().getLocation();
	}
	
	public void addTitle(Title title) {
		if (!titles.contains(title)) {
			titles.add(title);
			titleNames.add(title.getName());
		}
	}
	
	public Title addTitle(String name) {
		Title title = Title.getTitle(name);
		
		addTitle(title);
		
		return title;
	}
	
	public void removeTitle(String name) {
		Title title = Title.getTitle(name);
		
		titles.remove(title);
		titleNames.remove(name);
	}

	// ------
	// Non-persisted property mutators
	// ------
	public int getTimeSpent() {
		return timeSpent;
	}
	
	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getNewbieAttacks() {
		return newbieAttacks;
	}

	public int incrementNewbieAttacks() {
		return newbieAttacks++;
	}
	
	public ArrayList<Title> getTitles() {
		return titles;
	}
	
	public boolean isNewbieStalker() {
		return titles.contains(Title.getTitle(Title.newbieStalker));
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
	
	public String[] getTitleDescription(boolean self) {
		ArrayList<String> messages = new ArrayList<String>();
		
		String name = self ? "You" + ChatColor.RESET + " have" : getDisplayName(false) + ChatColor.RESET + " has";
		
		if (titles.size() == 0) {
			messages.add(ChatColor.AQUA + name + " no titles.");
		} else {
			messages.add(ChatColor.AQUA + name + " the following titles:");
			for (Title title : titles) {
				messages.add(ChatColor.DARK_AQUA + title.getDisplayName());
			}
		}
		
		return messages.toArray(new String[messages.size()]);
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
