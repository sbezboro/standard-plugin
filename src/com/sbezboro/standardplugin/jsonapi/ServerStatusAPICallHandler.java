package com.sbezboro.standardplugin.jsonapi;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sbezboro.standardplugin.SubPlugin;
import com.sbezboro.standardplugin.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simpleForBukkit.JSONObject;

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

		boolean minimal = false;
		if (payload != null && payload.get("minimal") != null) {
			minimal = (Boolean) payload.get("minimal");
		}

		ArrayList<Object> players = new ArrayList<Object>();

		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (SimplyVanishIntegration.isVanished(player)) {
				continue;
			}

			players.add(player.getInfo());
		}

		double load = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

		status.put("players", players);
		status.put("numplayers", players.size());
		status.put("maxplayers", Bukkit.getMaxPlayers());
		status.put("tps", EssentialsIntegration.getTPS());
		status.put("load", load);

		if (!minimal) {
			List<String> bannedUuids = new ArrayList<>();
			for (OfflinePlayer offlinePlayer : Bukkit.getBannedPlayers()) {
				bannedUuids.add(MiscUtil.getUuidString(offlinePlayer.getUniqueId()));
			}

			status.put("banned_uuids", bannedUuids);
		}

		for (SubPlugin subPlugin : plugin.getSubPlugins()) {
			status.putAll(subPlugin.additionalServerStatus(minimal));
		}

		return okResult(status);
	}
}
