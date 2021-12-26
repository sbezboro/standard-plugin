package com.sbezboro.standardplugin.jsonapi;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;
import org.bukkit.ChatColor;
import org.json.simpleForBukkit.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewMessageAPICallHandler extends APICallHandler {
	public NewMessageAPICallHandler(StandardPlugin plugin) {
		super(plugin, "new_message");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject handle(HashMap<String, Object> payload) {
		String fromUuid = (String) payload.get("from_uuid");
		String fromUsername = (String) payload.get("from_username");
		String toUuid = (String) payload.get("to_uuid");
		String url = (String) payload.get("url");
		Boolean noUser = (Boolean) payload.get("no_user");

		StandardPlayer toPlayer = plugin.getStandardPlayerByUUID(toUuid);
		if (toPlayer == null || !toPlayer.isOnline()) {
			return okResult();
		}

		StandardPlayer fromPlayer = null;

		if (fromUuid != null) {
			fromPlayer = plugin.getStandardPlayerByUUID(fromUuid);

			if (!fromPlayer.hasPlayedBefore()) {
				fromPlayer = null;
			}
		}

		String name;
		if (fromPlayer == null) {
			name = fromUsername;
		} else {
			name = fromPlayer.getDisplayName(false);
		}

		toPlayer.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.BOLD + "New message from " + name + ChatColor.DARK_GREEN + ChatColor.BOLD + "!");
		toPlayer.sendMessage(ChatColor.GREEN + "See it here: " + ChatColor.AQUA + url);

		if (noUser) {
			toPlayer.sendMessage(ChatColor.RED + "Note: you will need to create a website account first by typing /register");
		}

		return okResult();
	}
}
