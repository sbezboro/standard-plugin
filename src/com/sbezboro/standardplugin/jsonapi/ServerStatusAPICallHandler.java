package com.sbezboro.standardplugin.jsonapi;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.integrations.SimplyVanishIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ServerStatusAPICallHandler extends APICallHandler {

	public ServerStatusAPICallHandler(StandardPlugin plugin) {
		super(plugin, "server_status");
	}

	@Override
	public JSONObject handle(HashMap<String, Object> payload) {
		HashMap<String, Object> status = new HashMap<String, Object>();

		ArrayList<Object> players = new ArrayList<Object>();

		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (SimplyVanishIntegration.isVanished(player)) {
				continue;
			}
			try {
				if (player.isOnline()) {
					players.add(player.getInfo());
				}
			} catch (Exception e) {
				// Can happen if a player leaves as this handler is running
				plugin.getLogger().severe("Exception while getting player info");
				e.printStackTrace();
			}
		}

		double load = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

		status.put("players", players);
		status.put("numplayers", players.size());
		status.put("maxplayers", Bukkit.getMaxPlayers());
		status.put("tps", EssentialsIntegration.getTPS());
		status.put("load", load);

		return okResult(status);
	}
}
