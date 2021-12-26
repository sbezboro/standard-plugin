package com.sbezboro.standardplugin.jsonapi;

import com.sbezboro.standardplugin.StandardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simpleForBukkit.JSONObject;

import java.util.HashMap;

public class ServerSayAPICallHandler extends APICallHandler {

	public ServerSayAPICallHandler(StandardPlugin plugin) {
		super(plugin, "server_say");
	}

	@Override
	public JSONObject handle(HashMap<String, Object> payload) {
		String message = (String) payload.get("message");

		Bukkit.getServer().broadcastMessage("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "[Server] " + message);

		return okResult();
	}
}
