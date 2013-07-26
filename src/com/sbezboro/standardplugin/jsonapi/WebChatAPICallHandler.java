package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class WebChatAPICallHandler extends APICallHandler {

	public WebChatAPICallHandler(StandardPlugin plugin) {
		super(plugin, "web_chat");
	}

	@Override
	public Object handle(HashMap<String, Object> payload) {
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

		return false;
	}

	private boolean handleMessage(String username, String message) {
		StandardPlayer player = plugin.getStandardPlayer(username);

		if (player.isBanned()) {
			plugin.getLogger().warning(username + " has been blocked from web chat because they are banned.");
			return true;
		}

		Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Web Chat] " + ChatColor.AQUA + player.getDisplayName() + ChatColor.RESET + ": " + message);

		return true;
	}

	private boolean handleStatus(String type, String username) {
		StandardPlayer player = plugin.getStandardPlayer(username);

		if (player.isBanned()) {
			plugin.getLogger().warning(username + " has been blocked from web chat because they are banned.");
			return true;
		}

		String message;

		if (type.equals("enter")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has entered web chat";
		} else if (type.equals("exit")) {
			message = ChatColor.BLUE + "[Web Chat] " + ChatColor.YELLOW + player.getDisplayName(false) + " has left web chat";
		} else {
			return false;
		}

		Bukkit.getServer().broadcastMessage(message);

		return true;
	}
}
