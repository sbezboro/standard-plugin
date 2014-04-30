package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ForumPostAPICallHandler extends APICallHandler {

	public ForumPostAPICallHandler(StandardPlugin plugin) {
		super(plugin, "forum_post");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject handle(HashMap<String, Object> payload) {
		HashMap<String, String> data = (HashMap<String, String>) payload.get("data");

		String username = data.get("username");
		String forumName = data.get("forum_name");
		String topic = data.get("topic_name");
		String url = data.get("path");

		StandardPlayer player = plugin.getStandardPlayer(username);

		if (player.isForumMuted()) {
			if (player.isOnline()) {
				username = player.getDisplayName(false);
				player.sendMessage(ChatColor.RED + "The notification for your forum post has been hidden due to abuse and/or spam!");
			}

			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + username + ChatColor.DARK_AQUA + " posted in " + ChatColor.YELLOW + forumName);
		} else {
			username = player.getDisplayName(false);

			StandardPlugin.broadcast(ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + username + ChatColor.DARK_AQUA + " just posted in "
					+ ChatColor.YELLOW + forumName + ChatColor.DARK_AQUA + "!");
			StandardPlugin.broadcast(ChatColor.DARK_AQUA + "[Forum] Topic: " + ChatColor.YELLOW + topic);
			StandardPlugin.broadcast(ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + url);
		}

		return okResult();
	}
}
