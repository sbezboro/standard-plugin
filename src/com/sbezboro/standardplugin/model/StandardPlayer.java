package com.sbezboro.standardplugin.model;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.persistence.PersistedListProperty;
import com.sbezboro.standardplugin.persistence.PersistedProperty;
import com.sbezboro.standardplugin.persistence.PersistedPropertyDefinition;
import com.sbezboro.standardplugin.persistence.persistables.PersistableLocation;
import com.sbezboro.standardplugin.persistence.storages.PlayerStorage;
import com.sbezboro.standardplugin.persistence.storages.TitleStorage;
import com.sbezboro.standardplugin.tasks.PvpTimerTask;
import com.sbezboro.standardplugin.tasks.SpawnKillTimeoutTask;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardPlayer extends PlayerDelegate {
	private static final int PVP_TIMER_TIME = 600;  // 30 seconds
	
	private PersistedProperty<Boolean> forumMuted;
	private PersistedProperty<Boolean> pvpProtection;
	private PersistedProperty<PersistableLocation> bedLocation;
	private PersistedListProperty<String> titleNames;
	private PersistedProperty<Integer> endId;
	private PersistedProperty<Integer> pvpLogs;
	private PersistedProperty<Boolean> pvpLogged;
	private PersistedProperty<Integer> honeypotsDiscovered;

	private List<Title> titles;

	private int timeSpent;
	private int rank;

	private int newbieAttacks;
	
	private PvpTimerTask pvpTimerTask;
	private String lastAttackerUuid;

	private boolean lastDeathInPvp;
	private boolean lastDeathBySpawnkill;
	private boolean spawnKillProtection;
	private SpawnKillTimeoutTask spawnKillTimeoutTask;
	private long lastDeathTime;

	public StandardPlayer(final Player player, final PlayerStorage storage) {
		super(player, storage);
	}

	public StandardPlayer(final OfflinePlayer player, final PlayerStorage storage) {
		super(player, storage);
	}

	@Override
	public void createProperties() {
		forumMuted = createProperty(Boolean.class, "forum-muted");
		pvpProtection = createProperty(Boolean.class, "pvp-protection");
		bedLocation = createProperty(PersistableLocation.class, "bed");
		titleNames = createList(String.class, "titles");
		endId = createProperty(Integer.class, "end-id");
		pvpLogs = createProperty(Integer.class, "pvp-logs");
		pvpLogged = createProperty(Boolean.class, "pvp-logged");
		honeypotsDiscovered = createProperty(Integer.class, "honeypots-discovered");

		for (PersistedPropertyDefinition definition : StandardPlugin.getPlugin().getExtraPlayerPropertyDefinitions()) {
			createProperty(definition);
		}
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		
		titles = new ArrayList<Title>();

		TitleStorage titleStorage = StandardPlugin.getPlugin().getTitleStorage();
		ArrayList<String> titlesToRemove = new ArrayList<String>();
		for (String name : titleNames) {
			Title title = titleStorage.getTitle(name);

			if (title == null) {
				StandardPlugin.getPlugin().getLogger().severe("Player has non-existent title \"" + name + "\"!");
				titlesToRemove.add(name);
			} else {
				titles.add(title);
			}
		}

		for (String name : titlesToRemove) {
			titleNames.remove(name);
		}
	}

	public Block getTargetBlock(int range) {
		BlockIterator it = new BlockIterator(player, range);
		Block lastBlock = it.next();

		while (it.hasNext()) {
			lastBlock = it.next();

			if (lastBlock.getType() == Material.AIR) {
				continue;
			}

			break;
		}

		if (lastBlock.getType() == Material.AIR) {
			lastBlock = null;
		}

		return lastBlock;
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

	@Nullable
	public Location getBedLocation() {
		return bedLocation.getValue().getLocation();
	}

	public Location getBedLocationIfValid() {
		Location location = getBedLocation();

		if (location == null) {
			location = getBedSpawnLocation();
		}

		if (location != null) {
			Block bedBlock = location.getBlock();

			if (StandardPlugin.BED_BLOCKS.contains(bedBlock.getType())) {
				return location;
			}
		}

		return null;
	}

	public void addTitle(Title title) {
		if (!titles.contains(title)) {
			titles.add(title);
			titleNames.add(title.getIdentifier());
		}
	}

	public Title addTitle(String name) {
		Title title = StandardPlugin.getPlugin().getTitleStorage().getTitle(name);

		addTitle(title);

		return title;
	}

	public void removeTitle(Title title) {
		titles.remove(title);
		titleNames.remove(title.getIdentifier());
	}

	public void removeTitle(String name) {
		Title title = StandardPlugin.getPlugin().getTitleStorage().getTitle(name);

		removeTitle(title);
	}

	public int getEndId() {
		return endId.getValue();
	}

	public void setEndId(int endId) {
		this.endId.setValue(endId);
	}

	public int getPvpLogs() {
		return pvpLogs.getValue();
	}

	public void incrementPvpLogs() {
		pvpLogs.setValue(pvpLogs.getValue() + 1);
	}

	public int getHoneypotsDiscovered() {
		return honeypotsDiscovered.getValue();
	}

	public void incrementHoneypotsDiscovered() {
		honeypotsDiscovered.setValue(honeypotsDiscovered.getValue() + 1);
	}

	public boolean hasPvpLogged() {
		return pvpLogged.getValue();
	}

	public void setPvpLogged(boolean pvpLogged) {
		this.pvpLogged.setValue(pvpLogged);
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

	public List<Title> getTitles() {
		return titles;
	}

	public boolean hasTitle(String name) {
		for (Title title : titles) {
			if (title.getIdentifier().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public boolean isNewbieStalker() {
		return hasTitle(Title.NEWBIE_STALKER);
	}

	public String getLastAttackerUuid() {
		return lastAttackerUuid;
	}

	public void setLastAttacker(StandardPlayer player) {
		if (player == null) {
			lastAttackerUuid = null;
		} else {
			lastAttackerUuid = new String(player.getUuidString());
		}
	}

	public boolean isInPvp() {
		return pvpTimerTask != null && lastAttackerUuid != null &&
				StandardPlugin.getPlugin().getStandardPlayerByUUID(lastAttackerUuid).isOnline() && getHealth() > 0;
	}

	public boolean wasInPvp() {
		return pvpTimerTask != null;
	}

	public void setInPvp(StandardPlayer opponent) {
		if (opponent == null || this == opponent) {
			return;
		}

		if (pvpTimerTask == null) {
			sendMessage("You are now in PVP");
			if (hasTitle(Title.PVP_LOGGER)) {
				sendMessage(ChatColor.RED + "YOU WILL BE KILLED IF YOU PVP LOG");
			}
		} else {
			pvpTimerTask.cancel();
		}

		pvpTimerTask = new PvpTimerTask(StandardPlugin.getPlugin(), this);
		pvpTimerTask.runTaskLater(StandardPlugin.getPlugin(), PVP_TIMER_TIME);

		lastAttackerUuid = new String(opponent.getUuidString());
	}

	public void setNotInPvp() {
		if (pvpTimerTask != null) {
			sendMessage("You are no longer in PVP");
			pvpTimerTask.cancel();
			pvpTimerTask = null;
		}
	}

	public boolean isSpawnKillProtected() {
		return spawnKillProtection;
	}

	public boolean hasSpawnKillTimeout() {
		return spawnKillTimeoutTask != null;
	}

	public long getLastDeathTime() {
		return lastDeathTime;
	}

	public void setLastDeathTime() {
		lastDeathTime = System.currentTimeMillis();
	}

	public boolean lastDeathInPvp() {
		return lastDeathInPvp;
	}

	public boolean lastDeathBySpawnkill() {
		return lastDeathBySpawnkill;
	}

	public void setLastDeathInPvp(boolean value) {
		lastDeathInPvp = value;
	}

	public void setLastDeathBySpawnkill(boolean value) {
		lastDeathBySpawnkill = value;
	}

	public void enableSpawnKillProtection() {
		spawnKillProtection = true;

		if (spawnKillTimeoutTask != null) {
			spawnKillTimeoutTask.cancel();
		}

		StandardPlugin plugin = StandardPlugin.getPlugin();

		spawnKillTimeoutTask = new SpawnKillTimeoutTask(StandardPlugin.getPlugin(), this);
		spawnKillTimeoutTask.runTaskLater(plugin, plugin.getSpawnKillTimeout() * 20);

		StandardPlugin.getPlugin().getLogger().info("Enabling spawn kill protection and timeout for " +
				getDisplayName(false)
		);
	}

	public void disableSpawnKillTimeout() {
		if (spawnKillTimeoutTask != null) {
			spawnKillTimeoutTask.cancel();
			spawnKillTimeoutTask = null;
		}

		StandardPlugin.getPlugin().getLogger().info("Disabling spawn kill timeout for " +
						getDisplayName(false)
		);
	}

	public void disableSpawnKillProtection() {
		spawnKillProtection = false;

		StandardPlugin.getPlugin().getLogger().info("Disabling spawn kill protection for " +
						getDisplayName(false)
		);
	}

	public void onLeaveServer() {
		if (pvpTimerTask != null) {
			pvpTimerTask.cancel();
			pvpTimerTask = null;
		}

		if (spawnKillTimeoutTask != null) {
			spawnKillTimeoutTask.cancel();
			spawnKillTimeoutTask = null;
		}

		player = null;

		((PlayerStorage) storage).uncacheObject(getUuidString());
	}

	public void sendHome(World overworld) {
		Location location = getBedLocationIfValid();

		if (location == null) {
			teleport(overworld.getSpawnLocation());
		} else {
			teleport(location);
		}
	}

	public String getRankDescription(boolean self, int rank) {
		String message = "";

		Title broadcastedTitle = null;
		for (Title title : getTitles()) {
			if (title.isBroadcast()) {
				broadcastedTitle = title;
				break;
			}
		}

		if (self) {
			message = "You ";

			if (broadcastedTitle != null) {
				message += "are a " + ChatColor.GOLD + broadcastedTitle.getDisplayName() + ChatColor.WHITE + " and ";
			}

			message += "are ranked " + ChatColor.AQUA + MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!";
		} else {
			if (broadcastedTitle != null) {
				message += ChatColor.GOLD + broadcastedTitle.getDisplayName() + ChatColor.WHITE + " ";
			}

			message += ChatColor.AQUA + getDisplayName() + ChatColor.WHITE + " is ranked " + ChatColor.AQUA
					+ MiscUtil.getRankString(rank) + ChatColor.WHITE + " on the server!";
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

	public Map<String, Object> getInfo() {
		HashMap<String, Object> info = new HashMap<String, Object>();

		info.put("username", getName());
		info.put("uuid", getUuidString());
		info.put("address", getAddress().getAddress().getHostAddress());

		if (hasNickname()) {
			String nicknameAnsi = player.getDisplayName();
			String nickname = getDisplayName(false);

			info.put("nickname_ansi", nicknameAnsi);
			info.put("nickname", nickname);
		}

		info.put("rank", getRank());
		info.put("time_spent", getTimeSpent());

		info.put("is_pvp_protected", isPvpProtected());
		info.put("is_hunger_protected", isHungerProtected());

		info.put("world", getLocation().getWorld().getName());
		info.put("x", getLocation().getBlockX());
		info.put("y", getLocation().getBlockY());
		info.put("z", getLocation().getBlockZ());

		info.put("health", getHealth());

		info.put("pvp_logs", getPvpLogs());
		info.put("honeypots_discovered", getHoneypotsDiscovered());

		ArrayList<HashMap<String, Object>> titleInfos = new ArrayList<HashMap<String, Object>>();
		for (Title title : titles) {
			HashMap<String, Object> titleInfo = new HashMap<String, Object>();
			titleInfo.put("name", title.getIdentifier());
			titleInfo.put("display_name", title.getDisplayName());
			titleInfo.put("broadcast", title.isBroadcast());
			titleInfo.put("hidden", title.isHidden());
			titleInfos.add(titleInfo);
		}

		info.put("titles", titleInfos);

		return info;
	}

	public boolean hasNickname() {
		return EssentialsIntegration.hasNickname(this);
	}

	public String getDisplayName(boolean colored) {
		if (hasNickname()) {
			String name = EssentialsIntegration.getNickname(this);

			if (!colored) {
				return ChatColor.stripColor(name);
			}

			return name;
		}

		return getName();
	}

	public void sendTitleMessage(String title) {
		sendTitleMessage(title, null);
	}

	public void sendTitleMessage(String title, String subtitle) {
		sendTitleMessage(title, subtitle, 10, 100, 10);
	}

	public void sendTitleMessage(String title, String subtitle, int fadeIn, int time, int fadeOut) {
		StandardPlugin.sendTitleMessage(this, title, subtitle, fadeIn, time, fadeOut);
	}

	public void sendDelayedMessages(final String[] messages, int delayInSeconds) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(StandardPlugin.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (!isOnline()) {
					return;
				}

				sendMessage(messages);
			}
		}, delayInSeconds * 20);
	}

	public void sendDelayedMessages(String[] messages) {
		sendDelayedMessages(messages, 1);
	}

	public void sendDelayedMessage(String message, int delayInSeconds) {
		sendDelayedMessages(new String[] {message}, delayInSeconds);
	}

	public void sendDelayedMessage(String message) {
		sendDelayedMessages(new String[] {message});
	}

	public void mute() {
		EssentialsIntegration.setPlayerMuted(this, true);
	}

	public boolean isMuted() {
		return EssentialsIntegration.isPlayerMuted(this);
	}

	public void ban() {
		ban(null);
	}

	public void ban(String reason) {
		ban(reason, false);
	}

	public void ban(String reason, boolean withIp) {
		ban(reason, null, withIp);
	}

	public void ban(String reason, String source, boolean withIp) {
		if (withIp) {
			banIp();
		}

		BanList banList = Bukkit.getBanList(BanList.Type.NAME);

		banList.addBan(getName(), reason, null, source);

		if (isOnline()) {
			kickPlayer(reason);
		}
	}

	public void banIp() {
		if (isOnline()) {
			BanList banList = Bukkit.getBanList(BanList.Type.IP);

			banList.addBan(getAddress().getAddress().getHostAddress(), null, null, null);
		}
	}

	public String getUuidString() {
		return MiscUtil.getUuidString(getUniqueId());
	}

	@Override
	public String getDisplayName() {
		return getDisplayName(true);
	}
}
