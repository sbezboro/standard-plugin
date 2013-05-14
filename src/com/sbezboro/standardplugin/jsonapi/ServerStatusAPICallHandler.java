package com.sbezboro.standardplugin.jsonapi;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;

import me.asofold.bpl.simplyvanish.SimplyVanish;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ServerStatusAPICallHandler extends APICallHandler {

	public ServerStatusAPICallHandler(StandardPlugin plugin) {
		super(plugin);
	}

	@Override
	public Object handle(Object[] args) {
		HashMap<String, Object> status = new HashMap<String, Object>();
		
		ArrayList<Object> playerList = new ArrayList<Object>();
		
		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (!SimplyVanish.isVanished(player)) {
				HashMap<String, String> playerInfo = new HashMap<String, String>();
				playerInfo.put("username", player.getName());
				playerInfo.put("address", player.getAddress().getAddress().getHostAddress());
				
				if (player.hasNickname()) {
					playerInfo.put("nickname", player.getDisplayName(false));
				}
				
				playerList.add(playerInfo);
			}
		}
		
		ArrayList<Object> bannedPlayers = new ArrayList<Object>();
		for (OfflinePlayer player : Bukkit.getBannedPlayers()) {
			bannedPlayers.add(player.getName());
		}
		
		double cpuLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		
		status.put("players", playerList);
		status.put("numplayers", playerList.size());
		status.put("maxplayers", Bukkit.getMaxPlayers());
		status.put("banned_players", bannedPlayers);
		status.put("cpu_load", cpuLoad);
		
		return status;
	}

	@Override
	public String getName() {
		return "server_status";
	}

}
