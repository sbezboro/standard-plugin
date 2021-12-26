package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simpleForBukkit.JSONObject;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ForumPostAPICallHandler extends APICallHandler {

	public ForumPostAPICallHandler(StandardPlugin plugin) {
		super(plugin, "forum_post");
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject handle(HashMap<String, Object> payload) {
		String uuid = (String) payload.get("uuid");
		String username = (String) payload.get("username");
		String forumName = (String) payload.get("forum_name");
		String topic = (String) payload.get("topic_name");
		String url = (String) payload.get("path");
		boolean isNewTopic = (Boolean) payload.get("is_new_topic");

		String name;

		if (uuid == null) {
			name = username;
		} else {
			StandardPlayer player = plugin.getStandardPlayerByUUID(uuid);

			if (player.hasPlayedBefore()) {
				name = player.getDisplayName(false);

				if (player.isForumMuted()) {
					if (player.isOnline()) {
						player.sendMessage(ChatColor.RED + "The notification for your forum post has been hidden due to abuse and/or spam!");
					}

					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + name + ChatColor.DARK_AQUA + " posted in " + ChatColor.YELLOW + forumName);

					return okResult();
				}
			} else {
				name = username;
			}
		}

		String message = ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + name + ChatColor.DARK_AQUA;
		if (isNewTopic) {
			message += " created a new topic in " + ChatColor.YELLOW + forumName + ChatColor.DARK_AQUA + "!";
		} else {
			message += " just posted in " + ChatColor.YELLOW + forumName + ChatColor.DARK_AQUA + "!";
		}

		StandardPlugin.broadcast(message);
		StandardPlugin.broadcast(ChatColor.DARK_AQUA + "[Forum] Topic: " + ChatColor.YELLOW + topic);
		StandardPlugin.broadcast(ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + url);

		return okResult();
	}
}
