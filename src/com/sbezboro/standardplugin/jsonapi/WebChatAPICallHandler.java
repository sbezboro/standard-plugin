package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class WebChatAPICallHandler extends APICallHandler {

	public WebChatAPICallHandler(StandardPlugin plugin) {
		super(plugin, "web_chat");
	}

	@Override
	public JSONObject handle(HashMap<String, Object> payload) {
		String type = (String) payload.get("type");
		String username = (String) payload.get("username");

		if (type.equals("message")) {
			String message = (String) payload.get("message");
			return handleMessage(username, message);
		} else if (type.equals("enter")) {
			return handleStatus(type, username);
		} else if (type.equals("exit")) {
			return handleStatus(type, username);
		}

		return notHandledResult();
	}

	private JSONObject handleMessage(String username, String message) {
		StandardPlayer player = plugin.getStandardPlayer(username);

		if (player.isBanned()) {
			plugin.getLogger().warning(username + " has been blocked from web chat because they are banned.");
			
			return buildResult("BANNED", "banned");
		}

		StandardPlugin.broadcast(ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + ": " + message);

		return okResult();
	}

	private JSONObject handleStatus(String type, String username) {
		StandardPlayer player = plugin.getStandardPlayer(username);

		String message;

		if (type.equals("enter")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has entered web chat";
			if (player.isBanned()) {
				message += ", but they are banned!";
			}
		} else if (type.equals("exit")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has left web chat";
		} else {
			return notHandledResult();
		}

		StandardPlugin.broadcast(message);

		return okResult();
	}
}
