package com.sbezboro.standardplugin.persistence.storages;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.MiscUtil;
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

	public StandardPlayer getPlayerByUUID(String uuidString) {
		StandardPlayer standardPlayer = getObject(uuidString);

		if (standardPlayer == null) {
			UUID uuid = MiscUtil.getUuid(uuidString);

			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				standardPlayer = new StandardPlayer(offlinePlayer, this);
			} else {
				standardPlayer = new StandardPlayer(player, this);
			}

			cacheObject(uuidString, standardPlayer);
		} else if (standardPlayer.isOnline() && standardPlayer.getBasePlayer() == null) {
			UUID uuid = MiscUtil.getUuid(uuidString);

			standardPlayer.setPlayer(Bukkit.getPlayer(uuid));
			standardPlayer.setOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
		}

		return standardPlayer;
	}
}
