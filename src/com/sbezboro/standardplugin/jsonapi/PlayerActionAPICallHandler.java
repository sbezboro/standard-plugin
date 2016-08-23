package com.sbezboro.standardplugin.jsonapi;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class PlayerActionAPICallHandler extends APICallHandler {

	public PlayerActionAPICallHandler(StandardPlugin plugin) {
		super(plugin, "player_action");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject handle(HashMap<String, Object> payload) {
		String uuid = (String) payload.get("uuid");
		String ip = (String) payload.get("ip");
		boolean withIp = (Boolean) payload.get("with_ip");
		String action = (String) payload.get("action");
		String reason = (String) payload.get("reason");

		if (action == null) {
			return notHandledResult();
		}

		StandardPlayer player = null;
		if (uuid != null) {
			player = plugin.getStandardPlayerByUUID(uuid);
		}

		if (action.equals("ban")) {
			if (player == null) {
				return notHandledResult("Player not found by uuid");
			}
			return handleBan(player, reason, withIp);
		} else if (action.equals("ban_ip")) {
			return handleBanIp(ip);
		}

		return notHandledResult();
	}

	private JSONObject handleBan(StandardPlayer player, String reason, boolean withIp) {
		plugin.getLogger().warning("Banning player " + player.getDisplayName(false) +
				" via API: " + (reason != null ? reason : "no reason defined"));

		BanList banList = Bukkit.getBanList(BanList.Type.NAME);
		if (banList.isBanned(player.getName())) {
			plugin.getLogger().warning("Player " + player.getDisplayName(false) + " is already banned");
		}

		player.ban(reason, "API", withIp);

		return okResult();
	}

	private JSONObject handleBanIp(String ip) {
		plugin.getLogger().warning("Banning ip " + ip + " via API");

		BanList banList = Bukkit.getBanList(BanList.Type.IP);
		if (banList.isBanned(ip)) {
			plugin.getLogger().warning("IP " + ip + " is already banned");
		}

		banList.addBan(ip, null, null, "API");

		return okResult();
	}
}
