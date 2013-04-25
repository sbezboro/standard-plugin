package com.sbezboro.standardplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerSaver implements Runnable {

	@Override
	public void run() {
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			player.saveData();
		}
	}

}
