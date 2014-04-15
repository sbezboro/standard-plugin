package com.sbezboro.standardplugin.persistence.storages;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerStorage extends MultiFileStorage<StandardPlayer> {
	public PlayerStorage(StandardPlugin plugin) {
		super(plugin, "players");
	}

	@Deprecated
	public StandardPlayer getPlayer(String username) {
		StandardPlayer standardPlayer = getObject(username);

		if (standardPlayer == null) {
			Player player = Bukkit.getPlayer(username);
			if (player == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
				standardPlayer = new StandardPlayer(offlinePlayer, this);
			} else {
				standardPlayer = new StandardPlayer(player, this);
			}

			cacheObject(standardPlayer.getIdentifier(), standardPlayer);
		} else if (standardPlayer.isOnline() && standardPlayer.getBasePlayer() == null) {
			standardPlayer.setPlayer(Bukkit.getPlayer(username));
			standardPlayer.setOfflinePlayer(Bukkit.getOfflinePlayer(username));
		}

		return standardPlayer;
	}

	public StandardPlayer getPlayer(UUID uuid) {
		StandardPlayer standardPlayer = getObject(uuid);

		if (standardPlayer == null) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				standardPlayer = new StandardPlayer(offlinePlayer, this);
			} else {
				standardPlayer = new StandardPlayer(player, this);
			}

			cacheObject(standardPlayer.getIdentifier(), standardPlayer);
		} else if (standardPlayer.isOnline() && standardPlayer.getBasePlayer() == null) {
			standardPlayer.setPlayer(Bukkit.getPlayer(uuid));
			standardPlayer.setOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
		}

		return standardPlayer;
	}

	public void save(UUID uuid) {
		save(uuid.toString().replaceAll("-", ""));
	}

	protected StandardPlayer getObject(UUID uuid) {
		return getObject(uuid.toString().replaceAll("-", ""));
	}

	protected void cacheObject(UUID uuid, StandardPlayer player) {
		cacheObject(uuid.toString().replaceAll("-", ""), player);
	}

	public void uncacheObject(UUID uuid) {
		uncacheObject(uuid.toString().replaceAll("-", ""));
	}
}
