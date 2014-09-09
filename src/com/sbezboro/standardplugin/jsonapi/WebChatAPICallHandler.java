package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.integrations.EssentialsIntegration;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class WebChatAPICallHandler extends APICallHandler {

	public WebChatAPICallHandler(StandardPlugin plugin) {
		super(plugin, "web_chat");
	}

	@Override
	public JSONObject handle(HashMap<String, Object> payload) {
		String type = (String) payload.get("type");
		String username = (String) payload.get("username");
		String uuid = (String) payload.get("uuid");

		StandardPlayer player = null;

		if (uuid != null) {
			player = plugin.getStandardPlayerByUUID(uuid);

			if (!player.hasPlayedBefore()) {
				player = null;
			}
		}

		if (type.equals("message")) {
			String message = (String) payload.get("message");
			return handleMessage(player, username, message);
		} else if (type.equals("enter")) {
			return handleStatus(type, player, username);
		} else if (type.equals("exit")) {
			return handleStatus(type, player, username);
		}

		return notHandledResult();
	}

	private JSONObject handleMessage(StandardPlayer sender, String username, String message) {
		String name;

		if (sender == null) {
			name = username;
		} else {
			name = sender.getDisplayName();

			if (sender.isBanned()) {
				plugin.getLogger().warning(sender.getName() + " has been blocked from web chat because they are banned.");
				return buildResult("banned");
			}

			if (sender.isMuted()) {
				plugin.getLogger().warning(sender.getName() + " has been blocked from web chat because they are muted.");
				return buildResult("muted");
			}
		}
		
		String fullMessage = ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA + name + ChatColor.RESET + ": " + message;
		
		for (StandardPlayer player : plugin.getOnlinePlayers()) {
			if (!EssentialsIntegration.doesPlayerIgnorePlayer(player, sender)) {
				player.sendMessage(fullMessage);
			}
		}

		StandardPlugin.consoleWebchatMessage(fullMessage);

		return okResult();
	}

	private JSONObject handleStatus(String type, StandardPlayer player, String username) {
		String name;

		if (player == null) {
			name = username;
		} else {
			name = player.getDisplayName(false);
		}

		String message;

		if (type.equals("enter")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + name + " has entered web chat";
		} else if (type.equals("exit")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + name + " has left web chat";
		} else {
			return notHandledResult();
		}

		if (player != null && player.isBanned()) {
			plugin.getLogger().warning(ChatColor.stripColor(message));
		} else {
			StandardPlugin.broadcast(message);
		}

		return okResult();
	}
}
