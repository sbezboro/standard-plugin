package com.sbezboro.standardplugin.jsonapi;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;
import com.sbezboro.standardplugin.util.AnsiConverter;

public class ServerStatusAPICallHandler extends APICallHandler {
	
	public ServerStatusAPICallHandler(StandardPlugin plugin) {
		super(plugin, "server_status");
	}

	@Override
	public Object handle(HashMap<String, Object> payload) {
		HashMap<String, Object> status = new HashMap<String, Object>();
		
		ArrayList<Object> playerList = new ArrayList<Object>();
		
		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (SimplyVanishIntegration.isVanished(player)) {
				continue;
			}
			
			HashMap<String, String> playerInfo = new HashMap<String, String>();
			playerInfo.put("username", player.getName());
			playerInfo.put("address", player.getAddress().getAddress().getHostAddress());
			
			if (player.hasNickname()) {
				String nicknameAnsi = AnsiConverter.toAnsi(player.getDisplayName());
				String nickname = player.getDisplayName(false);
				playerInfo.put("nickname_ansi", nicknameAnsi);
				playerInfo.put("nickname", nickname);
			}
			
			playerList.add(playerInfo);
		}
		
		double load = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		
		status.put("players", playerList);
		status.put("numplayers", playerList.size());
		status.put("maxplayers", Bukkit.getMaxPlayers());
		status.put("tps", EssentialsIntegration.getTPS());
		status.put("load", load);
		
		return status;
	}
}
