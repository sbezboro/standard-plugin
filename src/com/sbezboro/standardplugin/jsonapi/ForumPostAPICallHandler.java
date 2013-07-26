package com.sbezboro.standardplugin.jsonapi;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.model.StandardPlayer;

public class ForumPostAPICallHandler extends APICallHandler {

	public ForumPostAPICallHandler(StandardPlugin plugin) {
		super(plugin, "forum_post");
	}

	@Override
	public Object handle(HashMap<String, Object> payload) {
		String username = (String) payload.get("username");
		String forumName = (String) payload.get("forum_name");
		String topic = (String) payload.get("topic_name");
		String url = (String) payload.get("path");

		StandardPlayer player = plugin.getStandardPlayer(username);

		if (player.isForumMuted()) {
			if (player.isOnline()) {
				username = player.getDisplayName(false);
				player.sendMessage(ChatColor.RED + "The notification for your forum post has been hidden due to abuse!");
			}

			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + username + ChatColor.DARK_AQUA + " posted in " + ChatColor.YELLOW + forumName);
		} else {
			username = player.getDisplayName(false);

			Bukkit.getServer().broadcastMessage(
					ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + username + ChatColor.DARK_AQUA + " just posted in " + ChatColor.YELLOW + forumName
							+ ChatColor.DARK_AQUA + "!");
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[Forum] Topic: " + ChatColor.YELLOW + topic);
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[Forum] " + ChatColor.YELLOW + url);
		}

		return true;
	}
}
