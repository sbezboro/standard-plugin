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
		String action = (String) payload.get("action");
		String reason = (String) payload.get("reason");

		if (uuid == null || action == null) {
			return notHandledResult();
		}

		StandardPlayer player = plugin.getStandardPlayerByUUID(uuid);

		if (player == null) {
			return notHandledResult("Player not found by uuid");
		}

		if (action.equals("ban")) {
			return handleBan(player, reason);
		}

		return notHandledResult();
	}

	private JSONObject handleBan(StandardPlayer player, String reason) {
		plugin.getLogger().warning("Banning player " + player.getDisplayName(false) +
				" via API: " + (reason != null ? reason : "no reason defined"));

		BanList banList = Bukkit.getBanList(BanList.Type.NAME);
		if (banList.isBanned(player.getName())) {
			plugin.getLogger().warning("Player " + player.getDisplayName(false) + " is already banned");
		}

		banList.addBan(player.getName(), reason, null, "API");

		if (player.isOnline()) {
			player.kickPlayer(reason);
		}

		return okResult();
	}
}
